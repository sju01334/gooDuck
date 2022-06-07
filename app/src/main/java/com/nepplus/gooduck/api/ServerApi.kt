package com.nepplus.gooduck.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.nepplus.gooduck.utils.ContextUtil
import com.nepplus.gooduck.utils.DateDeserializer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerApi {

    companion object {

        //        서버주소
        private val baseUrl = "https://api.gudoc.in"

        private var retrofit : Retrofit? = null

        fun getRetrofit(context : Context) : Retrofit {

            if (retrofit == null) {

//                자동으로 헤더를 달아주는 효과 발생

                val interceptor = Interceptor {
                    with(it) {
                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getLoginToken(context))
                            .build()
                        proceed(newRequest)
                    }
                }

//                gson에서 날짜 양식을 어떻게 파싱할 건지 => 추가 기능을 가진 gson 으로 생성
                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
//                        시차 보정기를 보조 도구로 채택
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .create()

//                retrofit : OkHttp의 확장판 => retrofit도 OkHttpClient 형태의 클라이언트 활용
//                클라이언트에게 우리가 만든 인터셉터를 달아주자( 클라이언트 커스터 마이징 )
                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(myClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            }

            return retrofit!!
        }

    }

}