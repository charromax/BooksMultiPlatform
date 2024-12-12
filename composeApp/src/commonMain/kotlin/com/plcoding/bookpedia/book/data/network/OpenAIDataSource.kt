package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookReviewDto
import com.plcoding.bookpedia.book.data.dto.Message
import com.plcoding.bookpedia.book.data.dto.QuestionDto
import com.plcoding.bookpedia.core.DEFAULT_MODEL
import com.plcoding.bookpedia.core.DEFAULT_TEMPERATURE
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class OpenAIDataSource(
    private val httpClient: HttpClient
) : CompletionsDataSource {
    override suspend fun getBookReview(
        apiKey: String,
        bookTitle: String,
        authorName: String
    ): Result<BookReviewDto, DataError.Remote> {
        val input = "Provide a brief review of the book '$bookTitle' written by '$authorName'. " +
                "No more than 6 lines and try to match the tone of the book itself"
        val question = QuestionDto(
            model = DEFAULT_MODEL,
            messages = listOf(
                Message(
                    role = "user",
                    content = input
                )
            ),
            temperature = DEFAULT_TEMPERATURE
        )
        return try {
            safeCall<BookReviewDto> {
                httpClient.post("https://api.openai.com/v1/chat/completions") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $apiKey")
                    }
                    setBody(question)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(DataError.Remote.UnknownError)
        }
    }
}