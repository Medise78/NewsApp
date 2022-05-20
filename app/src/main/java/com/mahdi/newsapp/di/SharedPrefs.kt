package com.mahdi.newsapp.di

import android.content.Context
import android.content.SharedPreferences
import com.mahdi.newsapp.util.LAST_CATEGORY_VISITED
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefs
{
     @Singleton
     @Provides
     fun provideSharedPrefs(
               @ApplicationContext context : Context ,
     ) : SharedPreferences =
               context.getSharedPreferences(LAST_CATEGORY_VISITED , Context.MODE_PRIVATE)
}