package com.example.budgetbee

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE userEmail = :userEmail LIMIT 1")
    suspend fun getUserByUserEmail(userEmail: String): UserEntity?
}