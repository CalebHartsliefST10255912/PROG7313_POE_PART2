package com.example.budgetbee

import android.content.Context
import androidx.room.*
import com.example.budgetbee.data.ExpenseEntryEntity
import com.example.budgetbee.data.ExpenseDao

@Database(entities = [UserEntity::class, GoalsEntity::class, CategoryEntity::class, ExpenseEntryEntity::class], version = 11, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalsDao(): GoalsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetBee_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
