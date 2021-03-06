package it.marta.whosings.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import it.marta.whosings.data.database.AppDatabase
import it.marta.whosings.data.database.User
import it.marta.whosings.data.repository.UserSession
import it.marta.whosings.utility.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {

    val usernameFound = SingleLiveEvent<Boolean>()
    private val context: Context by inject()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-whosings"
    ).fallbackToDestructiveMigration()
        .build()

    private val userDao = db.userDao()

    fun checkUsername(username: String) {
        UserSession.username = username
        viewModelScope.launch {
            usernameFound.value = getDbUser(username) != null
        }
    }

    fun addUser(username: String, name: String) {
        viewModelScope.launch {
            addDbUser(username, name)
        }
    }

    private suspend fun getDbUser(username: String) = withContext(Dispatchers.IO) {
        userDao.findByUsername(username)
    }

    private suspend fun addDbUser(username: String, name: String) = withContext(Dispatchers.IO) {
        userDao.insert(User(username, name, 0, 0))
    }

}