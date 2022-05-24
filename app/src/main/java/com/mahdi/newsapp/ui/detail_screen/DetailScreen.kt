package com.mahdi.newsapp.ui.detail_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mahdi.newsapp.R
import com.mahdi.newsapp.ui.common.WebView
import com.mahdi.newsapp.util.navMaskUrl
import com.mahdi.newsapp.util.navUnMaskUrl

@Composable
fun NewDetailScreen(
          url:String,
          navController : NavController
)
{
     val navUnMakUrl = navUnMaskUrl(url)
     
     Column {
          TopAppBar(
                    title = {
                         Text(text = stringResource(id = R.string.app_name))
                    },
                    navigationIcon = {
                         IconButton(onClick = { navController.navigateUp() }) {
                              Icon(imageVector = Icons.Default.ArrowBack ,
                                   contentDescription = "arrowBack")
                         }
                    },
                    backgroundColor = MaterialTheme.colors.primaryVariant
          )
          WebView(url = navUnMakUrl)
     }
}