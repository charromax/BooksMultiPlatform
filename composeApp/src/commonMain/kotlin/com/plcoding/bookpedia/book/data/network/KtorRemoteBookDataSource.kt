package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

private const val BASE_URL = "https://openlibrary.org/"
private const val SEARCH = "search.json"
private const val QUERY = "q"
private const val LIMIT = "limit"
private const val LANGUAGE = "language"
private const val FIELDS = "fields"
private const val FIELDS_REQUEST =
    "key,title,cover_edition_key,cover_i,author_name,author_key," +
            "language,first_publish_year,ratings_average,ratings_count," +
            "number_of_pages_median,edition_count"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
) : RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        limit: Int?,
        language: String
    ): Result<SearchResponseDto, DataError.Remote> = withContext(Dispatchers.IO) {
        safeCall {
            httpClient.get(urlString = "$BASE_URL$SEARCH") {
                parameter(QUERY, query)
                parameter(LIMIT, limit)
                parameter(LANGUAGE, language)
                parameter(FIELDS, FIELDS_REQUEST)
            }
        }
    }
}