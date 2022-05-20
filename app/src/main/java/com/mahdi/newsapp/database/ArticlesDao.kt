package com.mahdi.newsapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArticlesDao
{
    @Query("SELECT * FROM articles_entity")
    abstract fun getAllNews():Flow<List<ArticlesEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertNews(vararg articlesEntity : ArticlesEntity)
    
    @Delete
    abstract suspend fun deleteNews(articlesEntity : ArticlesEntity)
}