package com.example.bookbuddy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.bookbuddy.data.Book
import com.example.bookbuddy.ui.theme.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    viewModel: BookViewModel,
    bookId: Int?,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    // Fetch existing book data
    val book by produceState<Book?>(initialValue = null) {
        if (bookId != null) {
            value = viewModel.getBookById(bookId)
        }
    }

    // State variables with book data or empty defaults
    var title by remember { mutableStateOf(book?.title ?: "") }
    var author by remember { mutableStateOf(book?.author ?: "") }
    var genre by remember { mutableStateOf(book?.genre ?: "General") }
    var notes by remember { mutableStateOf(book?.notes ?: "") }
    var readingStatus by remember {
        mutableStateOf(book?.readingStatus ?: "To Read")
    }

    var readingStatusExpanded by remember {
        mutableStateOf(false)
    }

    var rating by remember {
        mutableIntStateOf(book?.rating ?: 0)
    }

    var ratingExpanded by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val isFormValid = title.isNotBlank() && author.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Book") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                isError = title.isBlank(),
                supportingText = {
                    if (title.isBlank()) {
                        Text("Title is required")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = author.isBlank(),
                supportingText = {
                    if (author.isBlank()) {
                        Text("Author is required")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Genre dropdown
            var expanded by remember { mutableStateOf(false) }
            Text(
                text = "Genre",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("General", "Fiction", "Non-Fiction", "Science", "Biography").forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                genre = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Reading Status",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = readingStatusExpanded,
                onExpandedChange = {
                    readingStatusExpanded = !readingStatusExpanded
                }
            ) {
                OutlinedTextField(
                    value = readingStatus,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = readingStatusExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = readingStatusExpanded,
                    onDismissRequest = {
                        readingStatusExpanded = false
                    }
                ) {
                    listOf(
                        "To Read",
                        "Currently Reading",
                        "Finished"
                    ).forEach { status ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = status,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                readingStatus = status
                                readingStatusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Rating (Optional)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = ratingExpanded,
                onExpandedChange = {
                    ratingExpanded = !ratingExpanded
                }
            ) {
                OutlinedTextField(
                    value = if (rating == 0) {
                        "Not rated"
                    } else {
                        "$rating / 5 stars"
                    },
                    onValueChange = {},
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = ratingExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = ratingExpanded,
                    onDismissRequest = {
                        ratingExpanded = false
                    }
                ) {
                    (0..5).forEach { value ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when (value) {
                                        0 -> "Not rated"
                                        1 -> "1 star"
                                        else -> "$value stars"
                                    },
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                rating = value
                                ratingExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            // Notes field
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFormValid && book != null) {

                        viewModel.updateBook(
                            book!!.copy(
                                title = title.trim(),
                                author = author.trim(),
                                genre = genre,
                                readingStatus = readingStatus,
                                rating = rating,
                                notes = notes.ifBlank { null }
                            )
                        )

                        focusManager.clearFocus()
                        onSave()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = isFormValid && book != null
            ) {
                Text("Save Changes")
            }
        }
    }


    LaunchedEffect(book) {
        if (book != null) {
            title = book!!.title
            author = book!!.author
            genre = book!!.genre
            notes = book!!.notes ?: ""

            readingStatus = book!!.readingStatus
            rating = book!!.rating

            focusRequester.requestFocus()
        }
    }

}