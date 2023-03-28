package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.local.dao.CryptoDao
import com.example.cryptocurrency.data.local.dao.LikedCryptoDao
import com.example.cryptocurrency.data.local.dao.PinnedCryptoDao
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
    private val cryptoDao: CryptoDao,
    private val pinnedCryptoDao: PinnedCryptoDao,
    private val likedCryptoDao: LikedCryptoDao
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