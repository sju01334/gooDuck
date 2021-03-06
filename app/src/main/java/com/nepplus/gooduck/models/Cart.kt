package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cart(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("product_id")
    val productId : Int,
    val product : Product,
    var isChecked: Boolean = false
): Serializable {
}