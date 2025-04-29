package com.example.budgetbee

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val userEmail: String,
    val password: String,
    val userName: String,
    val userPhone: String,
    val userDOB: String
)