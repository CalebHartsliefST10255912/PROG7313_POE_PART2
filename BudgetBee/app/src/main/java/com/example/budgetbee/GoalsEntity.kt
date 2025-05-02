package com.example.budgetbee

import androidx.room.Entity
import androidx.room.PrimaryKey
//This is the goals model
@Entity(tableName = "goals")
data class GoalsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val minMonthlyGoal: Double,
    val maxMonthlyGoal: Double,
    val otherGoal: String = ""
)
