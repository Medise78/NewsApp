package com.mahdi.newsapp.ui.authScreen

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
          private val sharedPreferences : SharedPreferences ,
) : ViewModel()
{
     
     override fun onCleared()
     {
          super.onCleared()
          with(sharedPreferences.edit()){
               putBoolean("LOGIN" , stayLogin.value)
               apply()
          }
     }
     
     private val _stayLogin = mutableStateOf(sharedPreferences.getBoolean("LOGIN" , false))
     val stayLogin:State<Boolean> get() = _stayLogin
     
     private val auth = Firebase.auth
     
     val currentUser = MutableStateFlow(auth.currentUser)
     
     fun signUpWithEmail(
               email : String ,
               password : String ,
               scaffoldState : ScaffoldState ,
               actionEmail : () -> Unit ,
     ) : Boolean
     {
          
          val success = auth.createUserWithEmailAndPassword(email , password).apply {
               addOnCompleteListener {
                    if (isSuccessful)
                    {
                         currentUser.value = auth.currentUser
                         actionEmail()
                    }
                    else
                    {
                         viewModelScope.launch {
                              scaffoldState.snackbarHostState.showSnackbar(exception?.message.toString())
                         }
                    }
               }
          }
          return if (success.isSuccessful) true else false
     }
     
     fun signInWithEmail(
               email : String ,
               password : String ,
               action : (String) -> Unit ,
               scaffoldState : ScaffoldState ,
     ):Boolean
     {
          val success = auth.signInWithEmailAndPassword(email , password).apply {
               addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                         currentUser.value = auth.currentUser
                         viewModelScope.launch {
                              scaffoldState.snackbarHostState.showSnackbar("${currentUser.value} LoggedIn")
                         }
                         action(email)
                    }
                    else
                    {
                         viewModelScope.launch {
                              scaffoldState.snackbarHostState.showSnackbar(exception?.message !!.trim())
                         }
                    }
               }
          }
          return if (success.isSuccessful) true else false
     }
     
     fun forgotPassword(email : String , scaffoldState : ScaffoldState)
     {
          auth.sendPasswordResetEmail(email).apply {
               addOnCompleteListener {
                    if (isSuccessful)
                    {
                         viewModelScope.launch { scaffoldState.snackbarHostState.showSnackbar("Success") }
                    }
                    else
                    {
                         viewModelScope.launch {
                              scaffoldState.snackbarHostState.showSnackbar(exception?.message !!)
                         }
                    }
               }
          }
     }
     
     fun signInAnonymously(action : (String) -> Unit)
     {
          auth.signInAnonymously().apply {
               addOnCompleteListener {
                    if (isSuccessful)
                    {
                         currentUser.value = auth.currentUser
                         action("Anonymous")
                    }
                    else
                    {
                         Log.e("Error" , exception?.message.toString())
                    }
               }
          }
     }
     
     fun signIn(login : Login , action : (String) -> Unit , scaffoldState : ScaffoldState)
     {
          when (login)
          {
               is Login.LoginUser ->
               {
                    signInAnonymously(action)
               }
               is Login.Email     ->
               {
                    signInWithEmail(login.email ,
                                    login.password ,
                                    action = action ,
                                    scaffoldState)
               }
          }
     }
     
     fun signUp(login : Login.Email , scaffoldState : ScaffoldState , action : () -> Unit)
     {
          signUpWithEmail(login.email , login.password , scaffoldState , action)
     }
     
     fun signOut(){
          auth.signOut()
     }
     
     fun stayLogin(
               context : Context ,
               email : String ,
               password : String ,
               action : (String) -> Unit ,
               scaffoldState : ScaffoldState ,
     )
     {
          val signIn = signInWithEmail(email , password , action , scaffoldState)
          if (signIn){
               auth.signOut()
          }
     }
}