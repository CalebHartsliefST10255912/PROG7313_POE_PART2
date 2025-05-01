package com.example.budgetbee

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserEntity::class, CategoryEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL(
                    """""
                        CREATE TABLE IF NOT EXISTS 'users'(
                            'userName' TEXT NOT NULL,
                            'password' TEXT NOT NULL,
                            'userEmail' TEXT NOT NULL,
                            'userPhone' TEXT NOT NULL,
                            'userDOB' TEXT NOT NULL,
                            'userId' INT NOT NULL,
                            PRIMARY KEY('userId')
                            )
                            """.trimIndent()

                )
            }
        }

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetbee_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    //There is and issue with the database where the userDate is saved as a Date in
                    //the database but roomDb can't store dates so this line of code below sorts out
                    //that issue and the date has been changed back to string
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}