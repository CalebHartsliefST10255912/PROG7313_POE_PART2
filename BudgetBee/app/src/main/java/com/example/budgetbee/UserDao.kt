package com.example.budgetbee

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao{
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE userEmail = :userEmail LIMIT 1")
    suspend fun getUserByUserEmail(userEmail: String): UserEntity?

    @Query("SELECT * FROM users WHERE userEmail = :userEmail AND userPhone = :userPhone LIMIT 1")
    fun getUserByUserEmailAndPhone(userEmail: String, userPhone: String): UserEntity?

    @Update
    fun updateUser(user: UserEntity)


    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserEntity>
    @Query("UPDATE users SET userName = :newName, userEmail = :newEmail, userPhone = :newPhone WHERE userEmail = :currentUserEmail")
    suspend fun updateUserInfo(currentUserEmail: String, newName: String, newEmail: String, newPhone: String)


}