package com.nepplus.gooduck.models

import java.io.Serializable

data class DataResponse(
    val user : UserData,
    val token : String,
    val banners : List<Banner>,
    val categories : List<Category>,
    val products : List<Product>,
    val carts : List<Cart>,
    val reviews : List<Review>,
    val replies : List<Reply>,
    val cards : List<Card>,
    val point_logs : List<Point>,
    val payments : List<Payment>
): Serializable {
}