package com.mahdi.newsapp.util

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://newsapi.org"
const val API_KEY = "2c3c30f91cf94c4988b0555e3d19e878"

const val LAST_CATEGORY_VISITED = "LAST_CATEGORY_VISITED"
const val DEFAULT_CATEGORY = "General"

const val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
const val DEFAULT_LANGUAGE = "US"

val fromCategory = mapOf(
          "General" to "general",
          "Entertainment" to "entertainment",
          "Business" to "business",
          "Health" to "health",
          "Science" to "science",
          "Sports" to "sports",
          "Technology" to "technology",
)

fun navMaskUrl(url : String) = url.replace("/" , "*")
fun navUnMaskUrl(url : String) = url.replace("*" , "/")

fun provideRetrofit(okHttpClient : OkHttpClient , moshi : Moshi):Retrofit{
     return Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(MoshiConverterFactory.create(moshi))
               .client(okHttpClient)
               .build()
}