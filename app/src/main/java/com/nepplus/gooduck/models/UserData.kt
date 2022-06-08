package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName

data class UserData(
    val id : Int,
    val provider : String,
    val uid : String,
    val email : String,
    val phone : String,
    @SerializedName("nick_name")
    val nickname : String,
    @SerializedName("profile_img")
    val profileImg : String,
    val point : Int
){
}