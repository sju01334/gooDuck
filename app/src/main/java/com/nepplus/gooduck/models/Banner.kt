package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

class Banner(
    val id : Int,
    @SerializedName("img_url")
    val imgUrl : String
) {
}