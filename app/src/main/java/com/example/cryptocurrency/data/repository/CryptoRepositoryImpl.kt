package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.local.CryptoDao
import com.example.cryptocurrency.data.remote.CryptoApi
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val cryptoApi: CryptoApi,
    private val cryptoDao: CryptoDao
): CryptoRepository {

    override fun getCryptoList(): Flow<Resource<List<Crypto>>> = flow {

        emit(Resource.Loading())

        val cachedCryptos = cryptoDao.getCryptos().map { it.toCrypto() }
        emit(Resource.Loading(data = cachedCryptos))

        try {
            val remoteCryptoList = cryptoApi.getCryptoList()
            cryptoDao.insertCryptos(remoteCryptoList.map { it.toCryptoEntity() })
        }catch (e: HttpException){
            emit(Resource.Error(
                message = e.message?:"Something went wrong!",
                data = cachedCryptos
            ))
        }catch (e: IOException){
            emit(Resource.Error(
                message = "Couldn't reach server! check your connection.",
                data = cachedCryptos
            ))
        }

        val newCachedPoets = cryptoDao.getCryptos().map { it.toCrypto() }
        emit(Resource.Success(newCachedPoets))

    }
}