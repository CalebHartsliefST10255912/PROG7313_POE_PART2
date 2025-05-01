package com.example.budgetbee

import androidx.room.*

@Dao
interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalsEntity)

    @Query("SELECT * FROM goals LIMIT 1")
    suspend fun getGoal(): GoalsEntity?

    @Query("DELETE FROM goals")
    suspend fun clearGoals()
}
