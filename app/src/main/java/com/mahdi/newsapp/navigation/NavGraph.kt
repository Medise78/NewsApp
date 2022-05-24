package com.mahdi.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahdi.newsapp.ui.authScreen.*
import com.mahdi.newsapp.ui.detail_screen.NewDetailScreen
import com.mahdi.newsapp.ui.favorite.FavoriteScreen
import com.mahdi.newsapp.ui.home_screen.HomeScreen
import com.mahdi.newsapp.ui.home_screen.HomeViewModel

@Composable
fun NavScreen()
{
     val navController = rememberNavController()
     val homeViewModel : HomeViewModel = hiltViewModel()
     val authViewModel : AuthViewModel = hiltViewModel()
     
     NavHost(navController = navController , startDestination = NavDestination.SIGNIN) {
          
          composable(NavDestination.SIGNIN) {
               LoginScreen(navController = navController ,
                           authViewModel = authViewModel ,
                           )
          }
          composable(NavDestination.SIGNUP) {
               SignUpScreen(authViewModel = authViewModel , navController = navController)
          }
          composable("${NavDestination.HOME}/{userName}") {
               HomeScreen(homeViewModel = homeViewModel ,
                          navController = navController ,
                          userName = it.arguments?.getString("userName") , authViewModel)
          }
          composable("${NavDestination.DETAILS}/{${NavDestination.URL_KEY}}", arguments = listOf(
                    navArgument(NavDestination.URL_KEY){
                         type = NavType.StringType
                    }
          )){
               NewDetailScreen(url = it.arguments?.getString(NavDestination.URL_KEY) !! ,
                               navController = navController)
          }
          composable(NavDestination.FAVORITE) {
               FavoriteScreen(homeViewModel = homeViewModel , navController = navController)
          }
     }
     
     
}