package com.example.budgetbee

import android.content.Context
import androidx.room.*
import com.example.budgetbee.data.ExpenseEntryEntity
import com.example.budgetbee.data.ExpenseDao
//Declares the room database with its entities and version
/*
*
* Google (2019). Save data in a local database using Room  |  Android Developers. [online] Android Developers. Available at: https://developer.android.com/training/data-storage/room.

‌
*
* Used this to help understand how to use roomdb
*
* */

@Database(entities = [UserEntity::class, GoalsEntity::class, CategoryEntity::class, ExpenseEntryEntity::class], version = 11, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

   //Abstract methods to get DAO instances
    abstract fun userDao(): UserDao
    abstract fun goalsDao(): GoalsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        //Singleton instance of the database
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
