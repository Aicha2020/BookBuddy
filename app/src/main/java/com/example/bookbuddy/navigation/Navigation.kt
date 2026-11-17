package com.example.bookbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookbuddy.screens.AddBookScreen
import com.example.bookbuddy.screens.BookDetailScreen
import com.example.bookbuddy.screens.BookListScreen
import com.example.bookbuddy.screens.EditBookScreen
import com.example.bookbuddy.ui.theme.BookViewModel

@Composable
fun BookNavigation(viewModel: BookViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "bookList"
    ) {
        // Book List Screen
        composable("bookList") {
            BookListScreen(
                viewModel = viewModel,
                onAddBook = { navController.navigate("addBook") },
                onBookClick = { bookId -> navController.navigate("book/$bookId") }
            )
        }

        // Add Book Screen
        composable("addBook") {
            AddBookScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        // book detail screen
        composable(
            route = "book/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->

            BookDetailScreen(
                viewModel = viewModel,
                bookId = backStackEntry.arguments?.getInt("bookId"),
                onBack = { navController.popBackStack() },
                onEdit = { bookId ->
                    navController.navigate("editBook/$bookId")
                },
                onDelete = {
                    navController.popBackStack()
                }
            )

        }

        // Edit Book Screen
        composable(
            route = "editBook/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            EditBookScreen(
                viewModel = viewModel,
                bookId = backStackEntry.arguments?.getInt("bookId"),
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
    }
}