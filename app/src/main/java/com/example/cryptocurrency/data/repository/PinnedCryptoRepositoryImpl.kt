package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.local.dao.PinnedCryptoDao
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PinnedCryptoRepositoryImpl @Inject constructor(
    private val pinnedCryptoDao: PinnedCryptoDao,
): PinnedCryptoRepository {

    override fun getPinnedCryptoList(): Flow<Resource<List<Crypto>>> = flow {
        emit(Resource.Loading())
        try {
            val cachedPinnedCryptos = pinnedCryptoDao.getPinnedCryptos().map { it.toCrypto() }
            emit(Resource.Success(data = cachedPinnedCryptos))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't get pinned cryptos."))
        }
    }

    override fun pinCrypto(crypto: Crypto): Flow<Resource<Long>> = flow {
        emit(Resource.Loading())
        try {
            pinnedCryptoDao.insertPinnedCryptos(crypto.toPinnedCryptoEntity())
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't pin crypto."))
        }
    }

    override fun unPinCrypto(cryptoId: Int): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            pinnedCryptoDao.deletePinnedCryptos(cryptoId)
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't unPin crypto."))
        }
    }

}