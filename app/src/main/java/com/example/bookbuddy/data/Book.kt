package com.example.bookbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")  // entoty to be stored in table
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String = "General", // Default value
    val readingStatus: String = "To Read", // Default status
    val rating: Int = 0, // 0 means unrated
    val notes: String? = null,
    val dateAdded: Long = System.currentTimeMillis(),
    val coverUri: String? = null
) {
    // Helper function for validation
    fun isValid(): Boolean {
        return title.isNotBlank() && author.isNotBlank()
        }
}