package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

data class Cart(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("product_id")
    val productId : Int,
    val product : Product
) {
}