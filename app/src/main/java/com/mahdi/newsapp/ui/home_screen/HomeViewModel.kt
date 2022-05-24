package com.mahdi.newsapp.ui.home_screen

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdi.newsapp.database.toDaoModel
import com.mahdi.newsapp.model.Articles
import com.mahdi.newsapp.model.Repository
import com.mahdi.newsapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
          private val repository : Repository ,
          private val sharedPreferences : SharedPreferences ,
) : ViewModel()
{
     
     override fun onCleared()
     {
          super.onCleared()
          with(sharedPreferences.edit()){
               putString(LAST_CATEGORY_VISITED , category.value)
               putString(SELECTED_LANGUAGE , language.value)
               apply()
          }
     }
     
     private val _category = mutableStateOf(sharedPreferences.getString(LAST_CATEGORY_VISITED ,
                                                                        DEFAULT_CATEGORY) !!)
     val category : State<String> get() = _category
     
     private val _language =
               mutableStateOf(sharedPreferences.getString(SELECTED_LANGUAGE , DEFAULT_LANGUAGE) !!)
     val language : State<String> get() = _language
     
     private val _currentQuery = mutableStateOf(_category.value)
     val currentQuery : State<String> get() = _currentQuery
     
     private val _news = MutableStateFlow<ApiState>(ApiState.Empty)
     val news : StateFlow<ApiState> get() = _news
     
     val favoriteArticles : StateFlow<List<Articles>> = repository.getAllNews()
               .stateIn(viewModelScope , SharingStarted.Eagerly , emptyList())
     
     init
     {
          updateNews()
     }
     
     fun updateQuery(query : String)
     {
          _currentQuery.value = query
     }
     
     fun updateNews()
     {
          viewModelScope.launch {
               _news.value = ApiState.Loading
               repository.topHeadline(
                         country = language.value.lowercase() ,
                         category = category.value.lowercase()
               ).catch { e ->
                    ApiState.Error(e)
               }.collect { data ->
                    _news.value = ApiState.Success(data)
               }
          }
     }
     
     fun addNews(articles : Articles)
     {
          viewModelScope.launch {
               repository.addNews(articles)
          }
     }
     
     fun removeNews(articles : Articles)
     {
          viewModelScope.launch {
               repository.deleteNews(articles.toDaoModel())
          }
     }
     
     fun likedArticles(articles : Articles , add : Boolean)
     {
          when (add)
          {
               true  -> addNews(articles)
               false -> removeNews(articles)
          }
     }
     
     fun searchNews(query : String , sortBy : String)
     {
          viewModelScope.launch {
               updateQuery(query)
               repository.searchNews(query , sortBy).collect { data ->
                    _news.value = ApiState.Success(data)
               }
          }
     }
     
     fun getCategory(query : String){
          _category.value = query
          updateQuery(query)
          updateNews()
     }
     
     fun getLanguage(query:String){
          _language.value = query
          updateNews()
     }
     
     fun isArticleLiked(articles : Articles) = favoriteArticles.value.contains(articles)
}