package com.example.budgetbee

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//@Database(entities = [UserEntity::class], version = 3, exportSchema = false)
@Database(entities = [UserEntity::class, GoalsEntity::class], version = 4, exportSchema = false)
//
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun goalsDao(): GoalsDao // added goals - vinay

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL(
                    """""
                        CREATE TABLE IF NOT EXISTS 'users'(
                            'username' TEXT NOT NULL,
                            'password' TEXT NOT NULL,
                            PRIMARY KEY('username')
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
                    "budgetBee_database" //i fixed the spelling - vinay
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