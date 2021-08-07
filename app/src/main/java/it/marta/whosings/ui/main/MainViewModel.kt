package it.marta.whosings.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import it.marta.whosings.data.database.AppDatabase
import it.marta.whosings.data.repository.UserSession
import it.marta.whosings.data.repository.WhoSingsRepository
import it.marta.whosings.data.vo.*
import it.marta.whosings.utility.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class MainViewModel(private val state: SavedStateHandle) : ViewModel(), KoinComponent {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val artists = SingleLiveEvent<List<SimpleDetailTrack>>()
    val lyric = SingleLiveEvent<String>()
    var win = SingleLiveEvent<ArrayList<Boolean>>()

    private val repository: WhoSingsRepository by inject { parametersOf(Dispatchers.IO) }
    private val match = arrayListOf<Boolean>()
    private val context: Context by inject()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-whosings"
    ).fallbackToDestructiveMigration()
        .build()
    private val userDao = db.userDao()

    fun getListTrack(answer: Boolean? = null) {
        viewModelScope.launch {
            if (answer == null || !isMatchWon(answer)) {
                loading.value = true
                state.get<List<Track>>(TOP100)?.let {
                    extractArtistsAndLyric(it)
                } ?: kotlin.run {
                    val response = repository.getListTrack()

                    response?.header?.errorText()?.let {
                        error.value = it
                    } ?: kotlin.run {
                        response?.getBody<TrackList>()?.let { body ->
                            body.trackList?.let { list ->
                                state.set(TOP100, list)
                                extractArtistsAndLyric(list)
                            }
                        }
                    }
                }

                loading.value = false
            }
        }

    }

    private suspend fun extractArtistsAndLyric(list: List<Track>) {
        val listArtist = list.map {
            SimpleDetailTrack(it.track?.trackId, it.track?.artistName)
        }

        val listRandomArtist = mutableListOf<SimpleDetailTrack>()

        while (listRandomArtist.size < 3) {
            val randomNumber = Random.nextInt(0, listArtist.size)
            if (!listRandomArtist.contains(listArtist[randomNumber]))
                listRandomArtist.add(listArtist[randomNumber])
        }

        val randomLyric = Random.nextInt(0, 3)
        val responseLyric =
            repository.getLyric(listRandomArtist[randomLyric].trackId ?: 0)

        responseLyric?.header?.errorText()?.let {
            error.value = it
        } ?: kotlin.run {
            val lyricBody = responseLyric?.getBody<Lyrics>()?.lyrics?.lyricsBody
            val listLine =
                lyricBody?.substring(0, lyricBody.indexOf("..."))
                    ?.split("\n")?.filter { it != "/n" && it != "" }
            listRandomArtist[randomLyric].stringTrack =
                listLine?.get(Random.nextInt(0, listLine.size))

            artists.value = listRandomArtist.toList()
            lyric.value = listRandomArtist[randomLyric].stringTrack ?: ""
        }
    }

    private fun isMatchWon(answer: Boolean): Boolean {
        match.add(answer)
        val matchesPlayedInThisSession = arrayListOf<Boolean>().apply {
            addAll(match)
        }
        if (matchesPlayedInThisSession.size == 5) {
            win.value = matchesPlayedInThisSession
            addMatchToDatabase(matchesPlayedInThisSession.count { it } >= 3)
            match.clear()
        }
        return matchesPlayedInThisSession.size == 5
    }


    private fun addMatchToDatabase(isWon: Boolean) {
        viewModelScope.launch {
            addMatch(isWon)
        }
    }

    private suspend fun addMatch(isWon: Boolean) = withContext(Dispatchers.IO) {
        UserSession.username?.let {
            val totalMatch = userDao.getTotalMatch(it)
            val match = userDao.getMatchWon(it)
            val user = userDao.findByUsername(it)?.copy(
                matchWon = if (isWon) match?.plus(
                    1
                ) else match, totalMatch = totalMatch?.plus(1)
            )
            user?.let {
                userDao.update(it)
            }
        }

    }


    companion object {
        const val TOP100 = "TOP100"
    }
}