package com.plcoding.bookpedia.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.bookdetail.BookDetailActions
import com.plcoding.bookpedia.book.presentation.bookdetail.BookDetailScreen
import com.plcoding.bookpedia.book.presentation.bookdetail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.booklist.BookListScreen
import com.plcoding.bookpedia.book.presentation.booklist.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph,
        ) {
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {
                composable<Route.BookList>(
                    exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }
                ) { entry ->
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel =
                        entry.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    LaunchedEffect(true) { selectedBookViewModel.onSelectedBook(null) }
                    BookListScreen(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            selectedBookViewModel.onSelectedBook(book)
                            navController.navigate(Route.BookDetail(book.id))
                        }
                    )
                }

                composable<Route.BookDetail>(
                    enterTransition = {
                        slideInHorizontally { initalOffset ->
                            initalOffset
                        }
                    },
                    exitTransition = {
                        slideOutHorizontally { initalOffset ->
                            initalOffset
                        }
                    }
                ) { entry ->
                    val selectedBookViewModel =
                        entry.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()
                    LaunchedEffect(selectedBook) {
                        selectedBook?.let {
                            viewModel.onAction(BookDetailActions.OnSelectedBookChange(it))
                        }
                    }
                    selectedBook?.let {
                        BookDetailScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}