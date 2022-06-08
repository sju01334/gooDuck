package com.nepplus.gooduck.models

data class BasicResponse(
    val code: Int,
    val message: String,
    val data: DataResponse
) {
}