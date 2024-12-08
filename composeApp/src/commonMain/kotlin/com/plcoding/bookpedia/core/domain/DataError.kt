package com.plcoding.bookpedia.core.domain

sealed interface DataError: Error {
    enum class Remote: DataError {
        RequestTimeout,
        TooManyRequests,
        ServerError,
        SerializationError,
        NoInternetConnection,
        UnknownError
    }

    enum class Local: DataError {
        DiskFull,
        UnknownError
    }
}