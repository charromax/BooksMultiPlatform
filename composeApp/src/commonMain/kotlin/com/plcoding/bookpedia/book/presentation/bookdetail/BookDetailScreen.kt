package com.plcoding.bookpedia.book.presentation.bookdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.description_not_available
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.bookdetail.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentation.bookdetail.components.BookChip
import com.plcoding.bookpedia.book.presentation.bookdetail.components.ChipSize
import com.plcoding.bookpedia.book.presentation.bookdetail.components.TitledContent
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailContent(
        state = state,
        onAction = { action ->
            when (action) {
                BookDetailActions.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }

    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailContent(
    state: BookDetailState,
    onAction: (BookDetailActions) -> Unit,
) {
    BlurredImageBackground(
        imageUrl = state.book?.imageUrl ?: "",
        isFavorite = state.isFavorite,
        onFavoriteClick = { onAction(BookDetailActions.OnFavoriteClick) },
        onBackClick = { onAction(BookDetailActions.OnBackClick) },
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = state.book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.book.averageRating?.let { rating ->
                        TitledContent(
                            title = stringResource(Res.string.rating),
                            content = {
                                BookChip {
                                    Text(
                                        text = "${round((rating * 10) / 10.0)}"
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = SandYellow,
                                    )
                                }
                            }
                        )
                    }
                    state.book.numPages?.let { pages ->
                        TitledContent(
                            title = stringResource(Res.string.pages),
                            content = {
                                BookChip {
                                    Text(
                                        text = pages.toString()
                                    )
                                }
                            }
                        )
                    }
                }
                if (state.book.languages.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            state.book.languages.forEach { language ->
                                BookChip(
                                    size = ChipSize.Small,
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = language.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    AnimatedVisibility(visible = !state.isLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(Res.string.synopsis),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.align(Alignment.Start)
                                    .fillMaxWidth()
                                    .padding(
                                        top = 24.dp,
                                        bottom = 8.dp
                                    )
                            )
                            Text(
                                text = if (state.book.description.isNullOrBlank()) stringResource(
                                    Res.string.description_not_available
                                ) else state.book.description,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Justify
                            )
                        }

                    }
                }
            }
        }
    }
}