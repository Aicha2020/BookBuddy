package com.example.bookbuddy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookbuddy.data.Book
import com.example.bookbuddy.ui.theme.BookViewModel
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookViewModel,
    bookId: Int?,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: () -> Unit
) {
    // Observe Room so changes made on the edit screen appear immediately.
    val bookFlow = remember(bookId) {
        if (bookId != null) viewModel.observeBookById(bookId) else flowOf(null)
    }
    val book by bookFlow.collectAsState(initial = null)
    var showDeleteConfirmation by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    IconButton(onClick = {
                        if (bookId != null) onEdit(bookId)
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }

                    IconButton(
                        onClick = {
                            showDeleteConfirmation = true
                        },
                        enabled = book != null
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete book"
                        )
                    }
                }

            )
        }
    ) { padding ->
        if (book == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Book not found")
            }
        } else {
            BookDetailContent(
                modifier = Modifier.padding(padding),
                book = book!!
            )
        }
    }

    // Show confirmation before deleting a book
    if (showDeleteConfirmation) {
        book?.let { bookToDelete ->
            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmation = false
                },
                title = {
                    Text("Delete book?")
                },
                text = {
                    Text(
                        "Are you sure you want to delete " +
                                "\"${bookToDelete.title}\"? " +
                                "This action cannot be undone."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                            viewModel.deleteBook(bookToDelete)
                            onDelete()
                        }
                    ) {
                        Text(
                            text = "Delete",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

}

@Composable
private fun BookDetailContent(
    modifier: Modifier = Modifier,
    book: Book
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Book illustration placeholder

        // Book illustration beside title and author
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Book illustration
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(130.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📖",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            // Book title and author
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // Metadata Grid
        DetailGridItem("Genre", book.genre)
        DetailGridItem("Status", book.readingStatus)
        DetailGridItem(
            "Rating",
            if (book.rating == 0) "Not rated" else "${book.rating}/5"
        )
        DetailGridItem("Added", book.dateAdded.toFormattedDate())

        Spacer(modifier = Modifier.height(24.dp))

        // Notes Section

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "My Notes",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = book.notes ?: "No notes added",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        lineHeight = 26.sp
                    )
                )
            }
        }
    }
}


        @Composable
        private fun DetailGridItem(label: String, value: String) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Extension function for date formatting
        private fun Long.toFormattedDate(): String {
            return java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(this))
        }
