package com.example.bookbuddy.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // create
    @Insert
    suspend fun insert(book: Book): Long

    // Read
    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): Book?

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun observeBookById(bookId: Int): Flow<Book?>

    // update
    @Update
    suspend fun update(book: Book): Int // should return 1

    // delete
    @Delete
    suspend fun delete(book: Book): Int

    // additional useful queries
    @Query("SELECT * FROM books WHERE readingStatus = :status") // filter by reading status
    fun getBooksByStatus(status: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE :query OR author LIKE :query")
    fun searchBooks(query: String): Flow<List<Book>>
}