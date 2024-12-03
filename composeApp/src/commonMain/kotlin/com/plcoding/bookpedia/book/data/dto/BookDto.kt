package com.plcoding.bookpedia.book.data.dto

import com.plcoding.bookpedia.book.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    @SerialName("key") val key: String?,
    @SerialName("title") val title: String?,
    @SerialName("cover_i") val coverId: Int?,
    @SerialName("author_name") val authorName: List<String>?,
    @SerialName("language") val language: List<String>?,
    @SerialName("first_publish_year") val firstPublishYear: Int?,
    @SerialName("ratings_average") val ratingsAverage: Double?,
    @SerialName("ratings_count") val ratingsCount: Int?,
    @SerialName("number_of_pages_median") val numberOfPagesMedian: Int?,
    @SerialName("edition_count") val editionCount: Int?
) {
    fun toBook(): Book {
        return Book(
            id = key?.removePrefix("/works/") ?: "",
            title = title.orEmpty(),
            imageUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }.orEmpty(),
            authors = authorName.orEmpty(),
            description = null, // No field for description in JSON
            languages = language.orEmpty(),
            firstPublishYear = firstPublishYear?.toString(),
            averageRating = ratingsAverage,
            ratingCount = ratingsCount,
            numPages = numberOfPagesMedian,
            numEditions = editionCount ?: 0
        )
    }
}