@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package com.mahdi.newsapp.ui.home_screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.rememberImagePainter
import com.mahdi.newsapp.model.Articles
import com.mahdi.newsapp.navigation.NavDestination
import com.mahdi.newsapp.ui.authScreen.AuthViewModel
import com.mahdi.newsapp.util.ApiState
import com.mahdi.newsapp.util.fromCategory
import com.mahdi.newsapp.util.navMaskUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
       homeViewModel : HomeViewModel ,
       navController : NavController ,
       userName : String? ,
       authViewModel : AuthViewModel ,
)
{
      val home = homeViewModel.news.collectAsState()
      val currentQuery = homeViewModel.currentQuery.value

      val backdropScaffoldState =
             rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)

      val updateCategory : (String) -> Unit = { cat ->
            homeViewModel.getCategory(cat)
      }

      val updateLanguage : (String) -> Unit = { lan ->
            homeViewModel.getLanguage(lan)
      }

      val onClickDetail : (String) -> Unit = { url ->
            val navMask = navMaskUrl(url)
            navController.navigate("${NavDestination.DETAILS}/$navMask")
      }

      val search : (String) -> Unit = { query ->
            homeViewModel.searchNews(query , sortBy = "publishedAt")
      }

      val favoriteLike = homeViewModel::likedArticles
      val navToFavorite : () -> Unit = {
            navController.navigate(NavDestination.FAVORITE)
      }
      val isFavoriteLiked : (Articles) -> Boolean = homeViewModel::isArticleLiked

      NewHomeScreenContent(
            news = home.value ,
            userName = userName ,
            onLogOut = {
                  authViewModel.signOut()
                  navController.navigate(NavDestination.SIGNIN)
                  NavOptions.Builder().setPopUpTo(NavDestination.SIGNIN , false).build()
            } ,
            onChangeLanguage = updateLanguage ,
            currentQuery = currentQuery ,
            navToFavorite = navToFavorite ,
            search = search ,
            onNewsClick = onClickDetail ,
            onLike = favoriteLike ,
            isLiked = isFavoriteLiked ,
            onCategorySelected = updateCategory ,
            backdropScaffoldState = backdropScaffoldState ,
      )
}

@ExperimentalMaterialApi
@Composable
fun NewHomeScreenContent(
       news : ApiState ,
       userName : String? ,
       onLogOut : () -> Unit ,
       onChangeLanguage : (String) -> Unit ,
       currentQuery : String ,
       navToFavorite : () -> Unit ,
       search : (String) -> Unit ,
       onNewsClick : (String) -> Unit ,
       onLike : (Articles , Boolean) -> Unit ,
       isLiked : (Articles) -> Boolean ,
       onCategorySelected : (String) -> Unit ,
       backdropScaffoldState : BackdropScaffoldState ,
)
{
      val scaffoldState = rememberScaffoldState()
      val coroutineScope = rememberCoroutineScope()
      val contentState = rememberLazyListState()

      Scaffold(
            scaffoldState = scaffoldState ,
            floatingActionButton = {
                  if (contentState.firstVisibleItemIndex > 0)
                  {
                        Floating {
                              coroutineScope.launch {
                                    contentState.animateScrollToItem(0)
                              }
                        }
                  }
            } ,
            floatingActionButtonPosition = FabPosition.End ,
            drawerContent = {
                  Drawer(
                        userName = userName ,
                        onLogOut = onLogOut ,
                        onChangeLanguage = onChangeLanguage
                  )
            } ,
            isFloatingActionButtonDocked = true
      ) {
            when (news)
            {
                  is ApiState.Empty   ->
                  {
                        Text(text = "Empty")
                  }


                  is ApiState.Loading ->
                  {
                        CircularProgressIndicator(
                              modifier = Modifier
                                    .fillMaxSize(1f)
                                    .wrapContentSize(Alignment.Center)
                        )
                  }


                  is ApiState.Error   ->
                  {

                  }


                  is ApiState.Success ->
                  {
                        HomeScreenContent(
                              currentQuery = currentQuery ,
                              navToFavorite = navToFavorite ,
                              openDrawer = { coroutineScope.launch { scaffoldState.drawerState.open() } } ,
                              search = search ,
                              lazyListState = contentState ,
                              news = news.data ,
                              onNewsClick = onNewsClick ,
                              onLike = onLike ,
                              isLike = isLiked ,
                              onCategorySelected = onCategorySelected ,
                              backdropScaffoldState = backdropScaffoldState
                        )
                  }
            }
      }
}

@Composable
fun Floating(
       onClick : () -> Unit ,
)
{
      FloatingActionButton(
            onClick = onClick ,
            backgroundColor = MaterialTheme.colors.primaryVariant
      ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp , contentDescription = "")
      }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
       currentQuery : String ,
       navToFavorite : () -> Unit ,
       openDrawer : () -> Unit ,
       search : (String) -> Unit ,
       lazyListState : LazyListState ,
       news : List<Articles> ,
       onNewsClick : (String) -> Unit ,
       onLike : (Articles , Boolean) -> Unit ,
       isLike : (Articles) -> Boolean ,
       onCategorySelected : (String) -> Unit ,
       backdropScaffoldState : BackdropScaffoldState ,
)
{
      BackdropScaffold(
            appBar = {
                  com.mahdi.newsapp.ui.common.TopAppBar(
                        currentQuery = currentQuery ,
                        navToFavorite = navToFavorite ,
                        openDrawer = openDrawer ,
                        search = search
                  )
            } ,
            frontLayerContent = {
                  NewsList(
                        lazyListState = lazyListState ,
                        news = news ,
                        onNewsClick = onNewsClick ,
                        onLike = onLike ,
                        isLike = isLike
                  )
            } ,
            backLayerContent = {
                  CategoriesList(
                        categories = fromCategory.keys.toList() ,
                        onCategorySelected = onCategorySelected
                  )
            } ,
            scaffoldState = backdropScaffoldState ,
            frontLayerBackgroundColor = MaterialTheme.colors.background ,
            frontLayerScrimColor = Color.Unspecified ,
            backLayerBackgroundColor = MaterialTheme.colors.primary ,
            persistentAppBar = true

      )
}

@Composable
fun Drawer(
       userName : String? ,
       onLogOut : () -> Unit ,
       modifier : Modifier = Modifier ,
       onChangeLanguage : (String) -> Unit ,
)
{
      var expanded by remember {
            mutableStateOf(false)
      }

      val languageList = listOf<String>("US" , "RU")

      var selectedText : String by remember {
            mutableStateOf(languageList[0])
      }

      Surface(color = MaterialTheme.colors.primarySurface) {

            Column(
                  verticalArrangement = Arrangement.spacedBy(8.dp) ,
                  modifier = modifier
                        .fillMaxSize()
                        .padding(24.dp , top = 48.dp)
            ) {

                  Text(text = "User ${userName?.substringBefore('@')}")
                  Spacer(modifier = Modifier.padding(10.dp))
                  Text(text = "News")
                  Divider()
                  Text(text = "Language")
                  Spacer(modifier = Modifier.padding(8.dp))
                  Button(onClick = { expanded = true } ,
                         colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(text = selectedText , modifier = Modifier.alpha(0.6f))
                        DropdownMenu(expanded = expanded ,
                                     onDismissRequest = { expanded = false }) {
                              listOf("US" , "RU").forEach { language ->
                                    DropdownMenuItem(onClick = {
                                          onChangeLanguage(language)
                                          expanded = false
                                          selectedText = language
                                    }) {
                                          Text(text = language , color = Color.Black)
                                    }
                              }
                        }
                  }
                  Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp) ,
                      contentAlignment = Alignment.BottomCenter) {
                        TextButton(onClick = onLogOut ,
                                   colors = ButtonDefaults.buttonColors(
                                         backgroundColor = Color.White) ,
                                   modifier = Modifier
                                         .fillMaxWidth(0.7f)
                                         .align(
                                               Alignment.BottomCenter)) {
                              Text(text = "Logout" , color = Color.Black)
                        }
                  }

            }
      }
}

@Composable
fun CategoriesList(
       categories : List<String> ,
       onCategorySelected : (String) -> Unit ,
)
{
      val scrollState = rememberScrollState()

      Row(
            modifier = Modifier.horizontalScroll(scrollState) ,
            horizontalArrangement = Arrangement.Center
      ) {
            for (i in categories)
            {
                  CategoryItem(category = i , onCategorySelected = onCategorySelected)
            }
      }
}

@Composable
fun CategoryItem(
       category : String ,
       onCategorySelected : (String) -> Unit ,
)
{
      Text(
            text = category ,
            style = MaterialTheme.typography.subtitle2 ,
            modifier = Modifier
                  .padding(15.dp)
                  .clickable { onCategorySelected(category.lowercase()) }
      )
}

@Composable
fun NewsList(
       lazyListState : LazyListState ,
       news : List<Articles> ,
       onNewsClick : (String) -> Unit ,
       onLike : (Articles , Boolean) -> Unit ,
       isLike : (Articles) -> Boolean ,
)
{
      LazyColumn(
            contentPadding = PaddingValues(10.dp) ,
            state = lazyListState ,
            verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
            items(news , key = { it.id }) {
                  ArticleItem(
                        articles = it ,
                        onNewsClick = onNewsClick ,
                        onLike = onLike ,
                        isLike = isLike(it)
                  )
            }
      }
}

@Composable
fun ArticleItem(
       modifier : Modifier = Modifier ,
       articles : Articles ,
       onNewsClick : (String) -> Unit ,
       onLike : (Articles , Boolean) -> Unit ,
       isLike : Boolean ,
)
{
      val context = LocalContext.current
      val clipboardManager = LocalClipboardManager.current

      Card(
            modifier = modifier
                  .wrapContentSize()
                  .clip(RoundedCornerShape(12.dp))
                  .pointerInput(Unit) {
                        detectTapGestures(
                              onLongPress = {
                                    clipboardManager.setText(AnnotatedString(articles.url))
                                    Toast
                                          .makeText(context ,
                                                    "Copied" ,
                                                    Toast.LENGTH_SHORT)
                                          .show()
                              } ,
                              onTap = {
                                    onNewsClick(articles.url)
                              }
                        )
                  }
      ) {
            Column {
                  Image(painter = rememberImagePainter(data = articles.urlToImage , builder = {
                        crossfade(true)
                        placeholder(null)
                  }) ,
                        contentDescription = "newsImage" ,
                        modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(2f) ,
                        contentScale = ContentScale.FillWidth
                  )
                  Column(
                        modifier = modifier.padding(8.dp)
                  ) {
                        Text(text = articles.title , style = MaterialTheme.typography.h6)
                        Text(text = articles.description)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                              Text(
                                    text = articles.author ,
                                    style = MaterialTheme.typography.subtitle2 ,
                                    modifier = modifier
                                          .weight(1f)
                                          .wrapContentWidth(Alignment.Start)
                              )
                              Row(modifier = modifier
                                    .wrapContentWidth(Alignment.End)
                                    .weight(1f)) {
                                    SocialButtons(
                                          articles = articles ,
                                          context = context ,
                                          onLike = onLike ,
                                          likeState = isLike
                                    )
                              }
                        }
                  }
            }
      }
}

@Composable
fun SocialButtons(
       articles : Articles ,
       context : Context ,
       onLike : (Articles , Boolean) -> Unit ,
       likeState : Boolean ,
)
{
      var isLiked by remember {
            mutableStateOf(likeState)
      }

      val likedButton = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp

      IconButton(onClick = {
            onLike(articles , ! isLiked)
            isLiked = ! isLiked
      }) {
            Icon(imageVector = likedButton , contentDescription = "likedButton")
      }

      IconButton(onClick = {
            val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(Intent.EXTRA_TEXT , articles.url)
                  type = "text/plain"
            }
            val sharedIntent = Intent.createChooser(sendIntent , null)
            context.startActivity(sharedIntent)
      }) {
            Icon(imageVector = Icons.Default.Share , contentDescription = "share")
      }
}