package com.plcoding.bookpedia.book.presentation.booklist

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

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

data class BookListState(
    val searchQuery: String = "Kotlin",
    val isLoading: Boolean = false,
    val searchResults: List<Book> = sampleBooks,
    val favoriteBooks: List<Book> = emptyList(),
    val error: UiText? = null,
    val selectedTab: Int = 0
)

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data class OnTabSelected(val index: Int): BookListAction
}
