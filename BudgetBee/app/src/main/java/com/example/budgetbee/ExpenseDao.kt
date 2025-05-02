/*package com.example.budgetbee.data

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

    @Query("SELECT * FROM expenses WHERE userId = :userId AND categoryId = :categoryId")
    suspend fun getExpensesForCategory(userId: Int, categoryId: Int): List<ExpenseEntryEntity>
} */

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
    suspend fun getExpensesByUser(userId: Int): List<ExpenseEntryEntity>
}

