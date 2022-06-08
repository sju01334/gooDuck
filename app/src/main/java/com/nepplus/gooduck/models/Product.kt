package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

class Product (
    val id : Int,
    val name : String,
    val price : Int,
    @SerializedName("image_url")
    val imageUrl : String,
    @SerializedName("small_category_id")
    val smallCategoryId : Int,
    @SerializedName("small_category")
    val smallCategory : SmallCategory,
    val store : Store,
    val reviews : List<Review>
        ){
}