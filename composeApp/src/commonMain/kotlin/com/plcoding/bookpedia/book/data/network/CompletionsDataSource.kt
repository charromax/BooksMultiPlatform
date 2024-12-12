package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookReviewDto
import com.plcoding.bookpedia.core.OPEN_AI_KEY
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface CompletionsDataSource {
    suspend fun getBookReview(
        apiKey: String = OPEN_AI_KEY,
        bookTitle: String,
        authorName: String
    ): Result<BookReviewDto, DataError.Remote>
}