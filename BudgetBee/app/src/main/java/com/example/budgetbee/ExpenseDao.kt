package com.example.budgetbee.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntryEntity)

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    suspend fun getExpensesForUser(userId: Int): List<ExpenseEntryEntity>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    suspend fun getExpensesForCategory(categoryId: Int): List<ExpenseEntryEntity>

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getAllExpensesForUser(userId: Int): List<ExpenseEntryEntity>


}
