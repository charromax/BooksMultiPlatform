package com.plcoding.bookpedia.book.presentation.booklist

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText


data class BookListState(
    val searchQuery: String = "Kotlin",
    val isLoading: Boolean = false,
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val error: UiText? = null,
    val selectedTab: Int = 0
)

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data class OnTabSelected(val index: Int): BookListAction
}
