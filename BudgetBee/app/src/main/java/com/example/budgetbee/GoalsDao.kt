package com.example.budgetbee

import androidx.room.*
//Will allow the program to use queries to access information on the database for goals
@Dao
interface GoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalsEntity)

    @Query("SELECT * FROM goals WHERE userId = :userId LIMIT 1")
    suspend fun getGoal(userId: Int): GoalsEntity?

    @Query("DELETE FROM goals WHERE userId = :userId")
    suspend fun clearGoals(userId: Int)
}

