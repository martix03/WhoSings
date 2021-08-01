package it.marta.whosings.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.marta.whosings.data.repository.WhoSingsRepository
import it.marta.whosings.data.vo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class MainViewModel(private val state: SavedStateHandle) : ViewModel(), KoinComponent {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val artists = MutableLiveData<List<SimpleDetailTrack>>()
    val lyric = MutableLiveData<String>()

    private val repository: WhoSingsRepository by inject { parametersOf(Dispatchers.IO) }
    fun getListTrack() {
        viewModelScope.launch {
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
            val lyricBody =  responseLyric?.getBody<Lyrics>()?.lyrics?.lyricsBody
            val listLine =
                lyricBody?.substring(0, lyricBody.indexOf("..."))
                    ?.split("\n")?.filter { it != "/n" && it != "" }
            listRandomArtist[randomLyric].stringTrack =
                listLine?.get(Random.nextInt(0, listLine.size))

            artists.value = listRandomArtist.toList()
            lyric.value = listRandomArtist[randomLyric].stringTrack ?: ""
        }
    }


    companion object {
        const val TOP100 = "TOP100"
    }
}