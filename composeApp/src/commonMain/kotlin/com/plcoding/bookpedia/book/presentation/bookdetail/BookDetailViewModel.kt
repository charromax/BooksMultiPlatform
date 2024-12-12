package com.plcoding.bookpedia.book.presentation.bookdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: BookRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            observeFavoriteStatus()
            getBookDetails()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    private val bookId: String = savedStateHandle.toRoute<Route.BookDetail>().id
    var faveJob: Job? = null

    fun onAction(action: BookDetailActions) {
        when (action) {
            is BookDetailActions.OnSelectedBookChange -> {
                _state.update {
                    it.copy(
                        book = action.book
                    )
                }
            }

            BookDetailActions.OnFavoriteClick -> viewModelScope.launch {
                if (state.value.isFavorite) {
                    bookRepository.deleteFromFavorites(bookId)
                } else {
                    state.value.book?.let { bookRepository.markFavorite(it) }
                }
            }

            else -> Unit
        }
    }

    private fun getBookDetails() = viewModelScope.launch {
        bookRepository.getBookDescription(bookId).onSuccess { description ->
            _state.update {
                it.copy(
                    isLoading = false, book = it.book?.copy(description = description)
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isLoading = false, error = it.error
                )
            }
        }
    }

    private fun observeFavoriteStatus() {
        faveJob?.cancel()
        faveJob = bookRepository
            .isBookFavorite(bookId)
            .onEach { isFav ->
                _state.update {
                    it.copy(
                        isFavorite = isFav
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}