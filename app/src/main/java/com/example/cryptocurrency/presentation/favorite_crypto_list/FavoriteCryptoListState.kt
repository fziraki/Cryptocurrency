package com.example.cryptocurrency.presentation.favorite_crypto_list

import com.example.cryptocurrency.domain.model.Crypto

data class FavoriteCryptoListState(
    val isLoading: Boolean = false,
    val cryptosToShow: List<Crypto> = emptyList(),
)
