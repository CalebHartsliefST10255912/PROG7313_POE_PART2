package com.example.budgetbee.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,         // FK to User
    val categoryId: Int,     // FK to Category
    val name: String,
    val amount: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val category: String,    // For display
    val description: String,
    val location: String
)
