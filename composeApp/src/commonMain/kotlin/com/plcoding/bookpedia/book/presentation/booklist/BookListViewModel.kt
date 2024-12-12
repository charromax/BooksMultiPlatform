package com.plcoding.bookpedia.book.presentation.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    private var cachedBooks: List<Book> = emptyList()
    private var searchJob: Job? = null
    private var favoriteJob: Job? = null

    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTab = action.index)
                }
            }

            else -> Unit
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(error = null, searchResults = cachedBooks)
                        }
                    }

                    query.length > 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        bookRepository
            .searchBooks(query)
            .onSuccess { results ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        searchResults = results
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.toUiText(),
                        searchResults = cachedBooks
                    )
                }
            }
    }

    private fun observeFavoriteBooks() {
        favoriteJob?.cancel()
        favoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favs ->
                _state.update {
                    it.copy(
                        favoriteBooks = favs
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
