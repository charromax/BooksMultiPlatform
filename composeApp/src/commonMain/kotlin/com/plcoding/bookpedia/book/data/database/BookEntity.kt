package com.plcoding.bookpedia.book.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.bookpedia.book.domain.Book

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val authors: List<String>,
    val languages: List<String>,
    val firstPublishYear: String?,
    val ratingsAverage: Double?,
    val ratingsCount: Int?,
    val numPagesMedian: Int?,
    val numEditions: Int
) {
    fun toBook(): Book = Book(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl ?: "",
        authors = authors,
        languages = languages,
        firstPublishYear = firstPublishYear,
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions
    )
}
