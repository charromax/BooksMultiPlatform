package com.plcoding.bookpedia.book.data.dto

import com.plcoding.bookpedia.book.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("docs") val results: List<BookDto>,
) {
    fun toBooks(): List<Book> {
        return results.map { it.toBook() }
    }
}