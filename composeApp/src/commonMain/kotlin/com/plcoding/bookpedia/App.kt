package com.plcoding.bookpedia

import androidx.compose.runtime.Composable
import com.plcoding.bookpedia.book.presentation.booklist.BookListScreen
import com.plcoding.bookpedia.book.presentation.booklist.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<BookListViewModel>()
    BookListScreen(
        viewModel = viewModel,
        onBookClick = {
            println("Clicked on $it")
        }
    )
}