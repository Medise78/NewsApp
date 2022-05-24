package com.mahdi.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahdi.newsapp.model.Articles
import java.util.*

@Entity(tableName = "articles_entity")
data class ArticlesEntity(
          @PrimaryKey @ColumnInfo(name = "id") val id : UUID ,
          @ColumnInfo(name = "author") val author : String ,
          @ColumnInfo(name = "title") val title : String ,
          @ColumnInfo(name = "url") val url : String ,
          @ColumnInfo(name = "url_to_image") val urlToImage : String ,
          @ColumnInfo(name = "published_at") val publishedAt : String ,
          @ColumnInfo(name = "content") val content : String ,
          @ColumnInfo(name = "description") val description : String ,
)

fun ArticlesEntity.toDomainModel() : Articles
{
     return Articles(
               id = id ,
               url = url ,
               urlToImage = urlToImage ,
               title = title ,
               content = content ,
               description = description ,
               author = author ,
               publishedAt = publishedAt
     )
}

fun Articles.toDaoModel() : ArticlesEntity
{
     return ArticlesEntity(
               publishedAt = publishedAt ,
               author = author ,
               description = description ,
               content = content ,
               title = title ,
               urlToImage = urlToImage ,
               url = url ,
               id = id
     )
}
