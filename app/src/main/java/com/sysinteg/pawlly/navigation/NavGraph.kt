// app/src/main/java/com/sysinteg/pawlly/navigation/NavGraph.kt

package com.sysinteg.pawlly.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sysinteg.pawlly.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen() }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun HomeScreen() {
    TODO("Not yet implemented")
}
