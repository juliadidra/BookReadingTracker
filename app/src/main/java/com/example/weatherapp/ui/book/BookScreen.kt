package com.example.weatherapp.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.example.weatherapp.ui.book.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen() {
    var showSearch by remember { mutableStateOf(false) }
    var showProfile by remember { mutableStateOf(false) }
    when {
        showSearch -> {
            BookSearchScreen(onBack = { showSearch = false })
            return
        }
        showProfile -> {
            ProfileScreen(onBack = { showProfile = false })
            return
        }
    }
    val currentBooks = listOf(
        Book("The Great Gatsby", "F. Scott Fitzgerald", "https://placeholder.com/book1.jpg", 65),
        Book("To Kill a Mockingbird", "Harper Lee", "https://placeholder.com/book2.jpg", 42),
        Book("1984", "George Orwell", "https://placeholder.com/book3.jpg", 78)
    )
    val savedBooks = listOf(
        Book("Pride and Prejudice", "Jane Austen", "https://placeholder.com/book4.jpg", 0),
        Book("The Hobbit", "J.R.R. Tolkien", "https://placeholder.com/book5.jpg", 0),
        Book("Harry Potter", "J.K. Rowling", "https://placeholder.com/book6.jpg", 0),
        Book("The Alchemist", "Paulo Coelho", "https://placeholder.com/book7.jpg", 0)
    )
    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    selected = true,
//                    onClick = { /* Navegação futura: Home dos livros */ },
//                    icon = { Icon(Icons.Default.Book, contentDescription = "Home") },
//                    label = { Text("Home") }
//                )
//                NavigationBarItem(
//                    selected = false,
//                    onClick = { /* Navegação futura: Busca */ },
//                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
//                    label = { Text("Buscar") }
//                )
//                NavigationBarItem(
//                    selected = false,
//                    onClick = { showProfile = true },
//                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//                    label = { Text("Perfil") }
//                )
//            }
//        }



    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Lendo agora",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentBooks) { book ->
                        CurrentReadingCard(book)
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Meus Livros",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { showSearch = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Livro")
                    }
                }
            }
            items(savedBooks) { book ->
                SavedBookItem(book)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentReadingCard(book: Book) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(280.dp),
        onClick = { }
    ) {
        Column {
            // Substituir AsyncImage por Box placeholder
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("Capa", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = book.author,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
                LinearProgressIndicator(
                    progress = book.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Text(
                    text = "${book.progress}%",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SavedBookItem(book: Book) {
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