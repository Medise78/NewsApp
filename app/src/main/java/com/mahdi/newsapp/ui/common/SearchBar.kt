package com.mahdi.newsapp.ui.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SearchBar(
          modifier : Modifier = Modifier ,
          onSubmit : (String) -> Unit ,
          isExpanded : Boolean = false ,
          onExpanded : (Boolean) -> Unit ,
)
{
     var text by remember {
          mutableStateOf("")
     }
     
     val focusRequester = remember {
          FocusRequester()
     }
     
     if (isExpanded)
     {
          TextField(
                    value = text , onValueChange = { textChange ->
               text = textChange
          },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                              keyboardType = KeyboardType.Text,
                              imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                              onSearch = {
                                   onSubmit(text)
                                   text = ""
                                   onExpanded(isExpanded)
                              }
                    ),
                    trailingIcon = {
                         IconButton(onClick = { onExpanded(isExpanded) }) {
                              Icon(imageVector = Icons.Default.Close , contentDescription = "close")
                         }
                    },
                    isError = true,
                    modifier = modifier
                              .aspectRatio(3f)
                              .focusRequester(focusRequester),
                    colors = TextFieldDefaults.textFieldColors(
                              textColor = MaterialTheme.colors.background,
                              backgroundColor = MaterialTheme.colors.background.copy(0f),
                              cursorColor = MaterialTheme.colors.background,
                              trailingIconColor = MaterialTheme.colors.background
                    )
          )
          LaunchedEffect(key1 = Unit){
               focusRequester.requestFocus()
          }
     }else{
          IconButton(onClick = { onExpanded(isExpanded) }) {
               Icon(imageVector = Icons.Default.Search , contentDescription = "")
          }
     }
}