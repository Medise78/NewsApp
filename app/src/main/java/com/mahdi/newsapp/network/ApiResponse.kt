package com.mahdi.newsapp.network

import com.mahdi.newsapp.model.Articles
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ApiResponse(
          val status:String,
          val totalResults:Int,
          val articles:List<ArticlesResponse>
)

@JsonClass(generateAdapter = true)
data class ArticlesResponse(
          val source:Source,
          val author:String?,
          val title:String?,
          val description:String?,
          val url:String?,
          val urlToImage:String?,
          val publishedAt:String?,
          val content:String?
)

@JsonClass(generateAdapter = true)
data class Source(
          val id:String?,
          val name:String?
)

fun ApiResponse.toDomainModel():List<Articles> = articles.map {
     Articles(
               id = UUID.nameUUIDFromBytes((it.author + it.publishedAt + it.url).toByteArray()),
               publishedAt = it.publishedAt?:"None",
               author = it.author?:"None",
               description = it.description?:"None",
               content = it.content?:"None",
               title = it.title?:"None",
               urlToImage = it.urlToImage?:"None",
               url = it.url?:"None"
     )
}