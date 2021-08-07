package it.marta.whosings.ui.profile

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

class ProfileViewModel : ViewModel(), KoinComponent {

    val user = MutableLiveData<User>()
    private val context: Context by inject()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-whosings"
    ) .fallbackToDestructiveMigration()
        .build()

    private val userDao = db.userDao()

    fun checkUsername() {
        viewModelScope.launch {
            UserSession.username?.let {
                user.value = getDbUser(it)

            }
        }
    }

    private suspend fun getDbUser(username: String) = withContext(Dispatchers.IO) {
        userDao.findByUsername(username)
    }
}