package com.example.bookbuddy

import android.app.Application
import com.example.bookbuddy.data.AppDatabase

class BookBuddyApplication : Application() {

    //Initialise database
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}
