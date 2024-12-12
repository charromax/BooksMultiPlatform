package com.plcoding.bookpedia.book.presentation.booklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.no_fave_results
import cmp_bookpedia.composeapp.generated.resources.no_search_results
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.booklist.components.BookList
import com.plcoding.bookpedia.book.presentation.booklist.components.SearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.PulseAnimation
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListContent(
        modifier = modifier,
        state = state,
        onAction = {
            when (it) {
                is BookListAction.OnBookClick -> onBookClick(it.book)
                else -> Unit
            }
            viewModel.onAction(it)
        }
    )
}

@Composable
fun BookListContent(
    modifier: Modifier = Modifier,
    state: BookListState,
    onAction: (BookListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
    val faveScrollState = rememberLazyListState()
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(state.selectedTab) {
        pagerState.animateScrollToPage(state.selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }

    LaunchedEffect(state.searchResults) {
        scrollState.animateScrollToItem(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = CenterHorizontally
    ) {
        SearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BookListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )

        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                TabRow(
                    selectedTabIndex = state.selectedTab,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    contentColor = SandYellow,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(it[state.selectedTab])
                        )
                    }
                ) {
                    Tab(
                        selected = state.selectedTab == 0,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(0))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = .5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    Tab(
                        selected = state.selectedTab == 1,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = .5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Top
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> {
                            if (state.isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Center
                                ) {
                                    PulseAnimation(modifier = Modifier.size(100.dp))
                                }
                            } else {
                                when {
                                    state.error != null -> {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = state.error.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    state.searchResults.isEmpty() -> {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = stringResource(Res.string.no_search_results),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                    }

                                    else -> {
                                        BookList(
                                            books = state.searchResults,
                                            onBookClick = {
                                                onAction(BookListAction.OnBookClick(it))
                                            },
                                            scrollState = scrollState,
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            if (state.favoriteBooks.isEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(Res.string.no_fave_results),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            } else {
                                BookList(
                                    books = state.favoriteBooks,
                                    onBookClick = {
                                        onAction(BookListAction.OnBookClick(it))
                                    },
                                    scrollState = faveScrollState,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}