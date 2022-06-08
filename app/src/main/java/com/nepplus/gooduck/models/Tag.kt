package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

class Tag(
    val id : Int,
    val tag : String,
    @SerializedName("review_id")
    val reviewId : Int
) {
}