package com.plcoding.bookpedia

import androidx.compose.runtime.*
import com.plcoding.bookpedia.book.presentation.booklist.BookListScreen
import com.plcoding.bookpedia.book.presentation.booklist.BookListViewModel

@Composable
fun App() {
    BookListScreen(
        viewModel = remember { BookListViewModel() },
        onBookClick = {
            println("Clicked on $it")
        }
    )
}