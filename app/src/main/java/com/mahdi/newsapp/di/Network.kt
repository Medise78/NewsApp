package com.mahdi.newsapp.di

import com.mahdi.newsapp.network.NewsApi
import com.mahdi.newsapp.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Network
{
     @Singleton
     @Provides
     fun provideRetrofit() : Retrofit
     {
          return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
     }
     
     @Singleton
     @Provides
     fun provideNewsApi(retrofit : Retrofit) : NewsApi = retrofit.create(NewsApi::class.java)
}