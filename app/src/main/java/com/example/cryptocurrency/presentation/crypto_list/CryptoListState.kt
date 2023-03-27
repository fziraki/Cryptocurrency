package com.example.cryptocurrency.presentation.crypto_list

import com.example.cryptocurrency.domain.model.Crypto

data class CryptoListState(
    val isLoading: Boolean = false,
    val pinnedCryptos: List<Crypto> = emptyList(),
    val cryptos: List<Crypto> = emptyList(),
){
    var cryptosToShow : List<Crypto> =
        cryptos.map { crypto ->
            crypto.isPinned = pinnedCryptos
                .any { pinnedCrypto ->
                    pinnedCrypto.id == crypto.id
                }
            crypto
        }.sortedByDescending {
            it.isPinned
        }
}
