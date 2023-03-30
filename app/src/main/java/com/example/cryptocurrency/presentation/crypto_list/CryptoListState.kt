package com.example.cryptocurrency.presentation.crypto_list

import com.example.cryptocurrency.domain.model.Crypto

data class CryptoListState(
    val isLoading: Boolean = false,
    val cryptosToShow: List<Crypto> = emptyList(),
    val isPagingLoading: Boolean = false,
    val endReached: Boolean = false,
    val page: Int = 1,
)
