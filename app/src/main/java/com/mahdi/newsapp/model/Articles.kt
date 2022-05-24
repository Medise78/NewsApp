package com.mahdi.newsapp.model

import java.util.*

data class Articles(
          val id:UUID,
          val title:String,
          val author:String,
          val description:String,
          val url:String,
          val urlToImage:String,
          val publishedAt:String,
          val content:String
)
