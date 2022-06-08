package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

data class Category(
    val id : Int,
    val name : String,
    @SerializedName("small_categories")
    val smallCategories : List<SmallCategory>
) {
}