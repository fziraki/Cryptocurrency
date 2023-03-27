package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.remote.CryptoApi
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val cryptoApi: CryptoApi
): CryptoRepository {

    override fun getCryptoList(): Flow<Resource<List<Crypto>>> = flow {

        emit(Resource.Loading())

        try {
            val remoteCryptoList = cryptoApi.getCryptoList().map { it.toCrypto() }
            emit(Resource.Success(remoteCryptoList))
        }catch (e: HttpException){
            emit(Resource.Error(message = e.message?:"Something went wrong!"))
        }catch (e: IOException){
            emit(Resource.Error(
                message = "Couldn't reach server! check your connection."))
        }

    }
}