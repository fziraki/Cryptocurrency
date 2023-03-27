package com.example.cryptocurrency.domain.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getCryptoList(): Flow<Resource<List<Crypto>>>
}