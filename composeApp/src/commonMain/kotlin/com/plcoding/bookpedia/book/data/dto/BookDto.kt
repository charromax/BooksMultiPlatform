package com.plcoding.bookpedia.book.data.dto

import com.plcoding.bookpedia.book.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    @SerialName("key") val key: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("cover_i") val coverAlternativeKey: Int? = null,
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("author_key") val authorKey: List<String>? = null,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("ratings_average") val ratingsAverage: Double? = null,
    @SerialName("ratings_count") val ratingsCount: Int? = null,
    @SerialName("number_of_pages_median") val numberOfPagesMedian: Int? = null,
    @SerialName("edition_count") val editionCount: Int? = null
) {
    fun toBook(): Book {
        return Book(
            id = key?.removePrefix("/works/") ?: "",
            title = title.orEmpty(),
            imageUrl = coverKey?.let { "https://covers.openlibrary.org/b/olid/$it-M.jpg" }
                ?: coverAlternativeKey?.let { "https://covers.openlibrary.org/b/olid/$it-M.jpg" }
                ?: "",
            authors = authorName.orEmpty(),
            description = null, // No field for description in JSON
            languages = languages.orEmpty(),
            firstPublishYear = firstPublishYear?.toString(),
            averageRating = ratingsAverage,
            ratingCount = ratingsCount,
            numPages = numberOfPagesMedian,
            numEditions = editionCount ?: 0
        )
    }
}