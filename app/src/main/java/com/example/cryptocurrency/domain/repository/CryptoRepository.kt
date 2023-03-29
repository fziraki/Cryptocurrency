package com.example.cryptocurrency.domain.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getCryptoList(page: Int, pageSize: Int): Flow<Resource<List<Crypto>>>
    fun getCryptoListByPage(page: Int, pageSize: Int): Flow<Result<List<Crypto>>>
    fun updateCrypto(cryptoId: Int, isPinned: Boolean): Flow<Resource<Int>>
}