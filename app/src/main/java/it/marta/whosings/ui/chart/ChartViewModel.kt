package it.marta.whosings.ui.chart

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import it.marta.whosings.data.database.AppDatabase
import it.marta.whosings.data.database.User
import it.marta.whosings.data.repository.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChartViewModel : ViewModel(), KoinComponent {

    val users = MutableLiveData<List<User>>()
    private val context: Context by inject()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-whosings"
    ).fallbackToDestructiveMigration()
        .build()

    private val userDao = db.userDao()

    fun getHigherScores() {
        viewModelScope.launch {
            UserSession.username?.let {
                users.value = getHigherUsers()
            }
        }
    }

    private suspend fun getHigherUsers() = withContext(Dispatchers.IO) {
        userDao.getHigherUsers()
    }
}