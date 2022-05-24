package com.mahdi.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
          entities = [ArticlesEntity::class] ,
          version = 1 ,
          exportSchema = false
)
abstract class ArticlesDatabase : RoomDatabase()
{
     abstract fun articlesDao() : ArticlesDao
}