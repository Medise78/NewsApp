package com.mahdi.newsapp.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.mahdi.newsapp.R

@Composable
fun TopAppBar(
          currentQuery : String ,
          navToFavorite : () -> Unit ,
          openDrawer : () -> Unit ,
          search : (String) -> Unit ,
)
{
     androidx.compose.material.TopAppBar(
               title = { Text(text = "${stringResource(id = R.string.app_name)}:${currentQuery.replaceFirstChar { it.uppercase() }}") } ,
               actions = { ActionBar(search = search , navToFavorite = navToFavorite) },
               navigationIcon = { IconButton(onClick = openDrawer) {
                    Icon(imageVector = Icons.Default.Menu , contentDescription = "")
               }},
               backgroundColor = MaterialTheme.colors.primaryVariant
     )
}

@Composable
fun ActionBar(
          search : (String) -> Unit ,
          navToFavorite : () -> Unit ,
)
{
     var isSearchExpanded by remember {
          mutableStateOf(false)
     }
     val onExpanded : (Boolean) -> Unit = { isSearchExpanded = ! isSearchExpanded }
     
     if (! isSearchExpanded)
     {
          IconButton(onClick = navToFavorite) {
               Icon(imageVector = Icons.Default.Favorite , contentDescription = "")
          }
     }
     SearchBar(onSubmit = search , onExpanded = onExpanded , isExpanded = isSearchExpanded)
}