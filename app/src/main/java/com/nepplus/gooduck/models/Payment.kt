package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Payment(
    val id : Int,
    @SerializedName("subscription_id")
    val subscriptionId : Int,
    val amount : Int,
    @SerializedName("created_at")
    val createdAt : String,
    val subscription : Subscription
) : Serializable {
}