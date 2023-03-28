package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.local.dao.LikedCryptoDao
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.LikedCryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class LikedCryptoRepositoryImpl @Inject constructor(
    private val likedCryptoDao: LikedCryptoDao
): LikedCryptoRepository {

    override fun getLikedCryptoList(): Flow<Resource<List<Crypto>>> = flow {
        emit(Resource.Loading())
        try {
            val cachedPinnedCryptos = likedCryptoDao.getLikedCryptos().map { it.toCrypto() }
            emit(Resource.Success(data = cachedPinnedCryptos))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't get liked cryptos."))
        }
    }

    override fun likeCrypto(crypto: Crypto): Flow<Resource<Long>> = flow {
        emit(Resource.Loading())
        try {
            likedCryptoDao.insertLikedCryptos(crypto.toLikedCryptoEntity())
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't like crypto."))
        }
    }

    override fun unLikeCrypto(cryptoId: Int): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            likedCryptoDao.deleteLikedCryptos(cryptoId)
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't unLike crypto."))
        }
    }
}