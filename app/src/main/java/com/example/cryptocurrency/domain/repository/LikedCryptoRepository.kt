package com.example.cryptocurrency.domain.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface LikedCryptoRepository {
    fun getLikedCryptoList(): Flow<Resource<List<Crypto>>>
    fun likeCrypto(crypto: Crypto): Flow<Resource<Long>>
    fun unLikeCrypto(cryptoId: Int): Flow<Resource<Int>>
}