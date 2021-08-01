package it.marta.whosings.data.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :username ")
    fun findByUsername(username: String): User?

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)
}