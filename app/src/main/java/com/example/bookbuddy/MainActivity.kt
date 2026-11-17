package com.example.bookbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookbuddy.navigation.BookNavigation
import com.example.bookbuddy.ui.theme.BookBuddyTheme
import com.example.bookbuddy.ui.theme.BookViewModel
import com.example.bookbuddy.ui.theme.BookViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookBuddyApp()
        }
    }
}

@Composable
private fun BookBuddyApp() {
    BookBuddyTheme {
        val application = LocalContext.current.applicationContext as BookBuddyApplication
        val bookViewModel: BookViewModel = viewModel(
            factory = BookViewModelFactory(application)
        )
        BookNavigation(viewModel = bookViewModel)
    }
}
