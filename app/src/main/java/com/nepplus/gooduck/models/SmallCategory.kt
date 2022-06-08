package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SmallCategory(
    val id : Int,
    val name : String,
    @SerializedName("large_category_id")
    val largeCategoryId : Int,
    var imageUrl : String
) : Serializable{
}