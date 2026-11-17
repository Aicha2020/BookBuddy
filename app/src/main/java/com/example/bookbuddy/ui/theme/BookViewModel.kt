package com.example.bookbuddy.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookbuddy.BookBuddyApplication
import com.example.bookbuddy.data.Book
import com.example.bookbuddy.data.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookViewModel(
    application: Application,
    private val bookDao: BookDao
) : AndroidViewModel(application) {

    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookDao.insert(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            bookDao.update(book)
        }
    }

    fun observeBookById(bookId: Int): Flow<Book?> = bookDao.observeBookById(bookId)

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookDao.delete(book)
        }
    }

    suspend fun getBookById(bookId: Int): Book? {
        return bookDao.getBookById(bookId)
    }
}

// Factory that uses BookBuddyApplication
class BookViewModelFactory(
    private val application: BookBuddyApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(
                application,
                application.database.bookDao()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}