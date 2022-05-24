package com.mahdi.newsapp.ui.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mahdi.newsapp.R
import com.mahdi.newsapp.navigation.NavDestination
import com.mahdi.newsapp.ui.home_screen.HomeViewModel
import com.mahdi.newsapp.ui.home_screen.NewsList
import com.mahdi.newsapp.util.navMaskUrl

@Composable
fun FavoriteScreen(
          homeViewModel : HomeViewModel ,
          navController : NavController ,
)
{
     val favoriteArticles = homeViewModel.favoriteArticles.collectAsState()
     val onLikeArticles = homeViewModel::likedArticles
     val isLiked = homeViewModel::isArticleLiked
     val favListState = rememberLazyListState()
     
     val onDetailClick : (String) -> Unit = { url ->
          val navMaskUrk = navMaskUrl(url)
          navController.navigate("${NavDestination.DETAILS}/$navMaskUrk")
     }
     
     if (favoriteArticles.value.isEmpty())
     {
          Text(
                    text = "Empty" ,
                    modifier = Modifier
                              .fillMaxSize()
                              .wrapContentSize(align = Alignment.Center) ,
                    style = MaterialTheme.typography.h6
          )
     }
     else
     {
          NewsList(
                    lazyListState = favListState ,
                    news = favoriteArticles.value ,
                    onNewsClick = onDetailClick ,
                    onLike = onLikeArticles ,
                    isLike = isLiked
          )
     }
}