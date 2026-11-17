package com.example.bookbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Book::class],
    version = 1, // initial database version 1
    exportSchema = true // required for Room to verify migrations
)
abstract class AppDatabase : RoomDatabase() {
    // Provides access to the DAO - Book Database Object
    //Room will automatically generate the implementation

    abstract fun bookDao(): BookDao

    companion object {
        // Singleton instance, volatile to ensure atomic access
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // get the singleton instance of the database
        // using double checked locking pattern for thread safety
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bookbuddy_database" // Database name
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
