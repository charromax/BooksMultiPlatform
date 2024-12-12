package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.book.data.database.BookEntity

data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String?,
    val languages: List<String>,
    val firstPublishYear: String?,
    val averageRating: Double?,
    val ratingCount: Int?,
    val numPages: Int?,
    val numEditions: Int
) {
    fun toBookEntity(): BookEntity {
        return BookEntity(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl,
            authors = authors,
            languages = languages,
            firstPublishYear = firstPublishYear,
            ratingsAverage = averageRating,
            ratingsCount = ratingCount,
            numPagesMedian = numPages,
            numEditions = numEditions
        )
    }
}
