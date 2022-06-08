package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

class SmallCategory(
    val id : Int,
    val name : String,
    @SerializedName("large_category_id")
    val largeCategoryId : Int,
    var imageUrl : String
) {
}