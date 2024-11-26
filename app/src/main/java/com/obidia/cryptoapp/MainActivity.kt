package com.obidia.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.obidia.cryptoapp.core.presentation.util.CoinListScreenRoute
import com.obidia.cryptoapp.crypto.presentation.cryptolist.cryptoListScreenRoute
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            CryptoAppTheme {
                SetNav(CoinListScreenRoute)
            }
        }
    }

    @Composable
    private fun SetNav(startDestination: Any) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = startDestination
        ) {
            cryptoListScreenRoute(::navigate)
//            coinDetailScreen(::navigate)
        }
    }

    private fun navigate(route: Any) {
        navController.navigate(route)
    }
}