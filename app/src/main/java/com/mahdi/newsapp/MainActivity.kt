package com.mahdi.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.mahdi.newsapp.navigation.NavScreen
import com.mahdi.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
     override fun onCreate(savedInstanceState : Bundle?)
     {
          super.onCreate(savedInstanceState)
          setContent {
               NewsAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize() ,
                            color = MaterialTheme.colors.background) {
                         NavScreen()
                    }
               }
          }
     }
}

