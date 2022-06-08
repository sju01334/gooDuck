package com.nepplus.gooduck.models

data class DataResponse(
    val user : UserData,
    val token : String,
    val banners : List<Banner>,
    val categories : List<Category>,
    val products : List<Product>,
    val carts : List<Cart>
) {
}