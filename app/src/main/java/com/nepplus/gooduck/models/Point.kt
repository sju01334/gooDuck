package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Point(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("payment_id")
    val paymentId : Int,
    @SerializedName("review_id")
    val reviewId : Int?,
    val amount : Int,
    val type : String,
    @SerializedName("created_at")
    val createdAt : String,
    val payment : Payment

) : Serializable {
}