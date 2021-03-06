package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Review (
    val id : Int,
    val title : String,
    val content : String,
    val score : Float,
    @SerializedName("review_count")
    val reviewCnt : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("product_id")
    val productId : Int,
    @SerializedName("thumbnail_img")
    val thumImg : String,
    @SerializedName("created_at")
    val createdAt : String,
    val user : UserData,
    val product : Product,
    val tags : List<Tag>

        ) : Serializable{
}