package com.mahdi.newsapp.ui.authScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.mahdi.newsapp.R
import com.mahdi.newsapp.navigation.NavDestination
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
          authViewModel : AuthViewModel ,
          navController : NavController ,
)
{
     val signup = authViewModel::signUp
     
     val scaffoldState = rememberScaffoldState()
     val scope = rememberCoroutineScope()
     
     var email by remember {
          mutableStateOf("")
     }
     
     var password by remember {
          mutableStateOf("")
     }
     
     val onSignUp : () -> Unit = {
          navController.navigate(NavDestination.SIGNIN)
     }
     
     val emailRequiements = ! email.contains("@") || email.length <= 10
     val passwordRequire = password.length < 5
     
     Scaffold(scaffoldState = scaffoldState) {
          Column(modifier = Modifier.fillMaxSize()) {
               Box(modifier = Modifier
                         .fillMaxWidth()
                         .height(280.dp)
                         .background(Color.Black) , contentAlignment = Alignment.Center) {
                    IconButton(onClick = { navController.navigateUp() } ,
                               modifier = Modifier
                                         .fillMaxSize(1f)
                                         .wrapContentSize(
                                                   Alignment.TopStart).padding(10.dp)) {
                         Icon(imageVector = Icons.TwoTone.ArrowBack ,
                              contentDescription = "" ,
                              tint = Color.White)
                    }
                    Image(painter = rememberImagePainter(data = R.drawable.bbc1) ,
                          contentDescription = "")
               }
               Box(
                         modifier = Modifier
                                   .fillMaxWidth()
                                   .fillMaxHeight() ,
                         contentAlignment = Alignment.Center
               ) {
                    Column(
                              modifier = Modifier.fillMaxWidth(0.7f) ,
                              verticalArrangement = Arrangement.Center ,
                              horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                         Text(text = "Signup" ,
                              style = MaterialTheme.typography.h6 ,
                              modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 5.dp))
                         Spacer(modifier = Modifier.height(35.dp))
                         TextField(
                                   value = email ,
                                   onValueChange = { email = it } ,
                                   singleLine = true ,
                                   isError = emailRequiements ,
                                   keyboardOptions = KeyboardOptions(
                                             keyboardType = KeyboardType.Email ,
                                             imeAction = ImeAction.Next ,
                                             autoCorrect = true ,
                                   ) ,
                                   label = {
                                        Text(text = "Email")
                                   } ,
                                   trailingIcon = {
                                        Icon(imageVector = Icons.Default.Close ,
                                             contentDescription = "" ,
                                             modifier = Modifier
                                                       .size(15.dp)
                                                       .clickable { email = "" })
                                   } ,
                                   colors = TextFieldDefaults.textFieldColors(
                                             backgroundColor = MaterialTheme.colors.background
                                   )
                         )
                         Spacer(modifier = Modifier.padding(10.dp))
                         TextField(
                                   value = password ,
                                   onValueChange = { password = it } ,
                                   singleLine = true ,
                                   isError = passwordRequire ,
                                   visualTransformation = PasswordVisualTransformation() ,
                                   keyboardOptions = KeyboardOptions(
                                             keyboardType = KeyboardType.Password ,
                                             imeAction = ImeAction.Send ,
                                   ) ,
                                   keyboardActions = KeyboardActions(
                                             onSend = {
                                                  if (email.isNotEmpty() && password.isNotEmpty())
                                                  {
                                                       signup(
                                                                 Login.Email(
                                                                           email = email ,
                                                                           password = password
                                                                 ) ,
                                                                 scaffoldState ,
                                                                 onSignUp
                                                       )
                                                  }
                                                  else if (email.isEmpty() || password.isEmpty())
                                                  {
                                                       scope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                      "Email or Password isEmpty")
                                                       }
                                                  }
                                                  else if (! email.contains('@'))
                                                  {
                                                       scope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                      "Please Enter Correct Email")
                                                       }
                                                  }
                                             }
                                   ) ,
                                   label = {
                                        Text(text = "Password")
                                   } ,
                                   trailingIcon = {
                                        Icon(imageVector = Icons.Default.Close ,
                                             contentDescription = "" ,
                                             modifier = Modifier
                                                       .size(15.dp)
                                                       .clickable {
                                                            password = ""
                                                       })
                                   } ,
                                   colors = TextFieldDefaults.textFieldColors(
                                             backgroundColor = MaterialTheme.colors.background ,
                                             trailingIconColor = Color.Black ,
                                   )
                         )
                         Spacer(modifier = Modifier.height(45.dp))
                         Button(onClick = {
                              if (email.isNotEmpty() && password.isNotEmpty())
                              {
                                   signup(
                                             Login.Email(
                                                       email = email ,
                                                       password = password
                                             ) ,
                                             scaffoldState ,
                                             onSignUp
                                   )
                              }
                              else if (email.isEmpty() || password.isEmpty())
                              {
                                   scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                                  "Email or Password isEmpty")
                                   }
                              }
                         } ,
                                modifier = Modifier
                                          .width(280.dp)
                                          .height(45.dp) ,
                                shape = RoundedCornerShape(10.dp) ,
                                colors = ButtonDefaults.buttonColors(
                                          backgroundColor = Color.Black ,
                                          contentColor = Color.White
                                )) {
                              Text(text = "Signup" , style = MaterialTheme.typography.button)
                         }
                    }
               }
          }
     }
}