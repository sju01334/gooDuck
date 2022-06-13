package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Reply(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
    @SerializedName("review_id")
    val reviewId : Int,
    val content : String,
    @SerializedName("created_at")
    val createdAt : String,
    val user : UserData
) : Serializable{
}