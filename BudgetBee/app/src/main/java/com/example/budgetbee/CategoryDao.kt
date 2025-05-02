package com.example.budgetbee

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetbee.data.ExpenseEntryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    suspend fun getAll(userId: Int): List<CategoryEntity>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    suspend fun getExpensesByCategory(categoryId: Int): List<ExpenseEntryEntity>
}
