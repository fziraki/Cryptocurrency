package com.example.cryptocurrency.presentation.favorite_crypto_list

import com.example.cryptocurrency.domain.model.Crypto

data class FavoriteCryptoListState(
    val isLoading: Boolean = false,
    val pinnedCryptos: List<Crypto> = emptyList(),
    val likedCryptos: List<Crypto> = emptyList(),
){
    var cryptosToShow : List<Crypto> =
        likedCryptos.map { crypto ->
            crypto.isLiked = true
            crypto.isPinned = pinnedCryptos
                .any { pinnedCrypto ->
                    pinnedCrypto.id == crypto.id
                }
            crypto
        }.sortedByDescending {
            it.isPinned
        }
}
