package com.nepplus.gooduck.api

import com.nepplus.gooduck.models.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    //user

    @GET("/user")
    fun getRequestMyInfo(): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("nick_name") nickname: String,
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("phone") phone: String,
    ): Call<BasicResponse>

    @GET("/user/check")
    fun getRequestUserCheck(
        @Query("type") type: String,
        @Query("value") value: String
    ): Call<BasicResponse>

    @GET("/user/find/email")
    fun getReqeustFindId(
        @Query("nick_name") nickname : String,
        @Query("phone") phone : String
    ) : Call<BasicResponse>

    @GET("/user/find/password")
    fun getReqeustFindPw(
        @Query("nick_name") nickname : String,
        @Query("email") email : String
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/social")
    fun postRequestSocialLogin(
        @Field("provider") provider : String,
        @Field("uid") uid : String,
        @Field("nick_name") nickname : String
    ) : Call<BasicResponse>

    //  main
    @GET("/main/banner")
    fun getRequestBanner() : Call<BasicResponse>

    //category
    @GET("/category")
    fun getRequestAllCategory() : Call<BasicResponse>

}