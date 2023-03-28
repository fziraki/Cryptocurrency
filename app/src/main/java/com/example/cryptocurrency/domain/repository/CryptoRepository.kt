package com.example.cryptocurrency.domain.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getCryptoList(): Flow<Resource<List<Crypto>>>
    fun getPinnedCryptoList(): Flow<Resource<List<Crypto>>>
    fun pinCrypto(crypto: Crypto): Flow<Resource<Long>>
    fun unPinCrypto(cryptoId: Int): Flow<Resource<Int>>
    fun getLikedCryptoList(): Flow<Resource<List<Crypto>>>
    fun likeCrypto(crypto: Crypto): Flow<Resource<Long>>
    fun unLikeCrypto(cryptoId: Int): Flow<Resource<Int>>

}