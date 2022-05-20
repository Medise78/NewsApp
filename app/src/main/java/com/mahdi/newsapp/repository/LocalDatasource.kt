package com.mahdi.newsapp.repository

import com.mahdi.newsapp.database.ArticlesDao
import com.mahdi.newsapp.database.ArticlesEntity
import com.mahdi.newsapp.database.toDaoModel
import com.mahdi.newsapp.database.toDomainModel
import com.mahdi.newsapp.model.Articles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDatasource @Inject constructor(
          private val articlesDao : ArticlesDao
)
{
     fun getAllNews():Flow<List<Articles>> = articlesDao.getAllNews().map { it.map { it.toDomainModel() } }
     
     suspend fun insertNews(articles : Articles) = articlesDao.insertNews(articles.toDaoModel())
     
     suspend fun deleteNews(articlesEntity : ArticlesEntity) = articlesDao.deleteNews(articlesEntity)
}