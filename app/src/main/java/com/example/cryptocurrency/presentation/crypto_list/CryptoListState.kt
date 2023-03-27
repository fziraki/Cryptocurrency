package com.example.cryptocurrency.presentation.crypto_list

import com.example.cryptocurrency.domain.model.Crypto

data class CryptoListState(
    val isLoading: Boolean = false,
    val cryptos: List<Crypto> = emptyList(),
)
