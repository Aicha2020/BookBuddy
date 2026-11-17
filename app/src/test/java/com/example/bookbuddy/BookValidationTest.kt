package com.example.bookbuddy

import com.example.bookbuddy.data.Book
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookValidationTest {
    @Test fun validBookHasTitleAndAuthor() {
        assertTrue(Book(title = "Dune", author = "Frank Herbert").isValid())
    }

    @Test fun blankTitleIsInvalid() {
        assertFalse(Book(title = "  ", author = "Frank Herbert").isValid())
    }

    @Test fun blankAuthorIsInvalid() {
        assertFalse(Book(title = "Dune", author = " ").isValid())
    }
}
