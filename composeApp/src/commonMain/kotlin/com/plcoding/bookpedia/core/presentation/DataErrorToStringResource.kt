package com.plcoding.bookpedia.core.presentation

import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.error_disk_full
import cmp_bookpedia.composeapp.generated.resources.error_no_internet
import cmp_bookpedia.composeapp.generated.resources.error_request_timeout
import cmp_bookpedia.composeapp.generated.resources.error_serialization
import cmp_bookpedia.composeapp.generated.resources.error_too_many_requests
import cmp_bookpedia.composeapp.generated.resources.error_unknown
import com.plcoding.bookpedia.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when (this) {
        DataError.Local.DiskFull -> Res.string.error_disk_full
        DataError.Local.UnknownError -> Res.string.error_unknown
        DataError.Remote.RequestTimeout -> Res.string.error_request_timeout
        DataError.Remote.TooManyRequests -> Res.string.error_too_many_requests
        DataError.Remote.ServerError -> Res.string.error_unknown
        DataError.Remote.SerializationError -> Res.string.error_serialization
        DataError.Remote.NoInternetConnection -> Res.string.error_no_internet
        DataError.Remote.UnknownError -> Res.string.error_unknown
    }
    return UiText.StringResourceId(stringRes)
}