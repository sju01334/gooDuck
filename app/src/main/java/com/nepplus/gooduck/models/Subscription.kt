package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Subscription(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("payment_id")
    val paymentId : Int,
    @SerializedName("review_id")
    val reviewId : Int?,
    @SerializedName("created_at")
    val createdAt : String,
    val product : Product
) : Serializable {
}