package com.example.cryptocurrency.presentation

sealed class Screen(val route: String) {
    object CryptoListScreen: Screen("crypto_list_screen")
}
