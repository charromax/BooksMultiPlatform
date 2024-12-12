package com.plcoding.bookpedia.book.presentation.bookdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.core.presentation.LightBlue

enum class ChipSize {
    Small, Regular
}

@Composable
fun BookChip(
    modifier: Modifier = Modifier,
    size: ChipSize = ChipSize.Regular,
    chipContent: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .widthIn(
                min = when (size) {
                    ChipSize.Small -> 50.dp
                    ChipSize.Regular -> 80.dp
                }
            )
            .clip(RoundedCornerShape(percent = 50))
            .background(LightBlue)
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) { chipContent() }

    }
}