package it.marta.whosings.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val username: String,
    @ColumnInfo val name: String?,
    @ColumnInfo val matchWon: Int?,
    @ColumnInfo val totalMatch: Int?
)