package it.marta.whosings.data.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :username ")
    fun findByUsername(username: String): User?

    @Query("SELECT totalMatch FROM user WHERE username LIKE :username ")
    fun getTotalMatch(username: String): Int?

    @Query("SELECT matchWon FROM user WHERE username LIKE :username ")
    fun getMatchWon(username: String): Int?

    @Insert
    fun insert(vararg users: User)

    @Update
    fun update(vararg users: User)

    @Delete
    fun delete(user: User)
}