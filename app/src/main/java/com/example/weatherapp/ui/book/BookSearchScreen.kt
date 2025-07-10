package com.example.weatherapp.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.book.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(onBack: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val searchResults = remember(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            listOf(
                Book("The Great Gatsby", "F. Scott Fitzgerald", "https://placeholder.com/book1.jpg", 0),
                Book("To Kill a Mockingbird", "Harper Lee", "https://placeholder.com/book2.jpg", 0),
                Book("1984", "George Orwell", "https://placeholder.com/book3.jpg", 0),
                Book("Pride and Prejudice", "Jane Austen", "https://placeholder.com/book4.jpg", 0),
                Book("The Hobbit", "J.R.R. Tolkien", "https://placeholder.com/book5.jpg", 0)
            ).filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.author.contains(searchQuery, ignoreCase = true)
            }
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Livros") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isSearching = it.isNotEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                placeholder = { Text("Buscar por título ou autor") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            if (isSearching) {
                if (searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhum livro encontrado")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchResults) { book ->
                            SearchResultItem(book)
                        }
                    }
                }
            } else {
                // Categorias populares (placeholder)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Categorias Populares",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    CategoryChips()
                }
            }
        }
    }
}

@Composable
fun CategoryChips() {
    val categories = listOf("Ficção", "Não-ficção", "Fantasia", "Sci-Fi", "Romance", "Mistério", "Biografia")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.take(4).forEach { category ->
                SuggestionChip(
                    onClick = { },
                    label = { Text(category) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.takeLast(3).forEach { category ->
                SuggestionChip(
                    onClick = { },
                    label = { Text(category) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultItem(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Substituir AsyncImage por Box placeholder
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("Capa", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.author,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 