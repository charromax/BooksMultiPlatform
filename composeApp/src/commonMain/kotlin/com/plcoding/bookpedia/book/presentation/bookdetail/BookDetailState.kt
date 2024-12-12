package com.plcoding.bookpedia.book.presentation.bookdetail

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.booklist.BookListAction

data class BookDetailState(
    val isLoading: Boolean = true,
    val book: Book? = null,
    val error: String? = null,
    val isFavorite: Boolean = false,

)

sealed interface BookDetailActions {
    data object OnBackClick: BookDetailActions
    data object OnFavoriteClick: BookDetailActions
    data class OnSelectedBookChange(val book: Book): BookDetailActions
}
