package com.mahdi.newsapp.network

import com.mahdi.newsapp.util.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi
{
     @GET("/v2/top-headlines")
     suspend fun topHeadlines(
               @Query("country") country:String,
               @Query("category") category:String,
               @Query("apiKey") apiKey:String = API_KEY
     ):ApiResponse
     
     @GET("/v2/everything")
     suspend fun searchNews(
               @Query("q") query:String,
               @Query("sortBy") sortBy:String,
               @Query("apiKey") apiKey:String = API_KEY
     ):ApiResponse
}