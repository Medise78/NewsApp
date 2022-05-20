package com.mahdi.newsapp.ui.common

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(
          url : String ,
)
{
     val context = LocalContext.current
     AndroidView(factory = {
          WebView(context).apply {
               webViewClient = WebViewClient()
               loadUrl(url)
          }
     })
}