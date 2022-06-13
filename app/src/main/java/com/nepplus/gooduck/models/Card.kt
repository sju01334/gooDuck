package com.nepplus.gooduck.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Card(
    val id : Int,
    @SerializedName("user_id")
    val  userId : Int,
    @SerializedName("card_num")
    val  cardNum : String,
    @SerializedName("card_nickname")
    val  cardNick : String,
    @SerializedName("mm_yy")
    val  mmyy : String,
    val birthday : String,
    @SerializedName("card_name")
    val  cardName : String,
    @SerializedName("card_code")
    val  cardCode : Int,
    @SerializedName("logo_img_url")
    val  cardLogo : String,
    val color : String,
    @SerializedName("is_main")
    val  isMain : Boolean,
    @SerializedName("is_deleted")
    val  isDeleted : Boolean,
    @SerializedName("billing_key")
    val  key : String,
    @SerializedName("created_at")
    val  createdAt : String,
    @SerializedName("updated_at")
    val  updatedAt : String,
) : Serializable {
}