package com.mahdi.newsapp.ui.authScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.mahdi.newsapp.R
import com.mahdi.newsapp.navigation.NavDestination
import kotlinx.coroutines.launch

sealed class Login{
     object LoginUser:Login()
     class Email(val email:String, val password:String):Login()
}

@Composable
fun LoginScreen(
          authViewModel : AuthViewModel ,
          navController : NavController ,
)
{
     val signIn = authViewModel::signIn
     val signInAnonymously = authViewModel::signInAnonymously
     
     val scaffoldState = rememberScaffoldState()
     val scope = rememberCoroutineScope()
     
     var email by remember {
          mutableStateOf("")
     }
     
     var password by remember {
          mutableStateOf("")
     }
     
     val onSignIn : (String) -> Unit = { user ->
          navController.navigate("${NavDestination.HOME}/$user")
     }
     
     val emailRequiements = ! email.contains("@") || email.length <= 10
     val passwordRequire = password.length < 5
     
     Scaffold(scaffoldState = scaffoldState) {
          Column(modifier = Modifier.fillMaxSize()) {
               Box(modifier = Modifier
                         .fillMaxWidth()
                         .height(280.dp)
                         .background(Color.Black) , contentAlignment = Alignment.Center) {
                    Image(painter = rememberImagePainter(data = R.drawable.bbc1) ,
                          contentDescription = "")
               }
               Spacer(modifier = Modifier.height(0.dp))
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
                         Text(text = "Login" ,
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
                                             imeAction = ImeAction.Next
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
                                                       signIn(
                                                                 Login.Email(
                                                                           email = email,
                                                                           password = password
                                                                 ),
                                                                 {
                                                                      onSignIn(email.substringAfter('@'))
                                                                 } ,
                                                                 scaffoldState
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
                                   signIn(
                                             Login.Email(
                                                       email = email ,
                                                       password = password
                                             ) ,
                                             {
                                                  onSignIn(email)
                                             } ,
                                             scaffoldState
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
                         } ,
                                modifier = Modifier
                                          .width(280.dp)
                                          .height(45.dp) ,
                                shape = RoundedCornerShape(10.dp) ,
                                colors = ButtonDefaults.buttonColors(
                                          backgroundColor = Color.Black ,
                                          contentColor = Color.White
                                )) {
                              Text(text = "SignIn" , style = MaterialTheme.typography.button)
                         }
                         Spacer(modifier = Modifier.height(10.dp))
                         Row(
                                   verticalAlignment = Alignment.CenterVertically ,
                                   horizontalArrangement = Arrangement.Center
                         ) {
                              TextButton(
                                        onClick = { authViewModel.forgotPassword(email, scaffoldState) } ,
                                        colors = ButtonDefaults.textButtonColors(
                                                  contentColor = Color.Black.copy(0.6f)
                                        )) {
                                   Text(text = "ForgotPassword?" ,
                                        style = TextStyle(textDecoration = TextDecoration.Underline))
                              }
                              Text(text = "or" , color = Color.Black.copy(0.6f))
                              TextButton(
                                        onClick = {
                                                  navController.navigate(NavDestination.SIGNUP)
                                        } ,
                                        colors = ButtonDefaults.textButtonColors(
                                                  contentColor = Color.Black.copy(0.6f)
                                        )) {
                                   Text(text = "SignUp" ,
                                        style = TextStyle(textDecoration = TextDecoration.Underline))
                              }
                         }
                         TextButton(onClick = {signIn(Login.LoginUser , onSignIn , scaffoldState)}) {
                              Text(text = "SignIn Anonymously" , color = Color.Black)
                         }
                    }
               }
          }
     }
}