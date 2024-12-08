package com.plcoding.bookpedia

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.booklist.BookListContent
import com.plcoding.bookpedia.book.presentation.booklist.BookListState
import com.plcoding.bookpedia.book.presentation.booklist.components.SearchBar

@Composable
@Preview
fun SearchBarPreview() {
    SearchBar(
        searchQuery = "Kotlin",
        onSearchQueryChange = {},
        onImeSearch = {}
    )
}

val sampleBooks = (1..50).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        authors = listOf("charr0max"),
        averageRating = 5.0,
        imageUrl = "",
        description = "This is a sample book description",
        numEditions = 1,
        firstPublishYear = "2024",
        languages = listOf(),
        ratingCount = null,
        numPages = null,
    )
}

@Composable
@Preview
fun BookListPreview() {
    BookListContent(
        state = BookListState(
            searchResults = sampleBooks
        ),
        onAction = {}
    )
}