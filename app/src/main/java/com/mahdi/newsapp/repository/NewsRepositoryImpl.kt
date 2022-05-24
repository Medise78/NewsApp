package com.mahdi.newsapp.repository

import com.mahdi.newsapp.database.ArticlesEntity
import com.mahdi.newsapp.database.toDomainModel
import com.mahdi.newsapp.model.Articles
import com.mahdi.newsapp.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
          private val localDatasource : LocalDatasource,
          private val remoteDatasource : RemoteDatasource
):Repository
{
     override fun topHeadline(country : String , category : String) : Flow<List<Articles>> = flow{
          emit(remoteDatasource.topHeadlines(country, category))
     }.flowOn(Dispatchers.IO)
     
     override fun searchNews(query : String , sortBy:String) : Flow<List<Articles>> = flow{
          emit(remoteDatasource.searchNews(query , sortBy))
     }.flowOn(Dispatchers.IO)
     
     override fun getAllNews() : Flow<List<Articles>>
     {
          return localDatasource.getAllNews()
     }
     
     override suspend fun addNews(articles : Articles)
     {
          localDatasource.insertNews(articles)
     }
     
     override suspend fun deleteNews(articlesEntity : ArticlesEntity)
     {
          localDatasource.deleteNews(articlesEntity)
     }
}