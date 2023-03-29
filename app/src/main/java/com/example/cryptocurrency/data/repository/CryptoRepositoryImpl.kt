package com.example.cryptocurrency.data.repository

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.local.dao.CryptoDao
import com.example.cryptocurrency.data.local.dao.LikedCryptoDao
import com.example.cryptocurrency.data.local.dao.PinnedCryptoDao
import com.example.cryptocurrency.data.remote.CryptoApi
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.delay
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

    override fun getCryptoList(page: Int, pageSize: Int): Flow<Resource<List<Crypto>>> = flow {

        val startingIndex = page * pageSize

        emit(Resource.Loading())

        val cachedCryptos = cryptoDao.getCryptosByPage(startingIndex, pageSize).map { it.toCrypto() }
        emit(Resource.Loading(data = cachedCryptos))

        try {
            val remoteCryptoList = cryptoApi.getCryptoList()
            cryptoDao.insertCryptos(
                remoteCryptoList.map { cryptoDto ->
                    cryptoDto.toCryptoEntity(
                        isPinned = pinnedCryptoDao.getPinnedCryptos().any{it.id == cryptoDto.id},
                        isLiked = likedCryptoDao.getLikedCryptos().any{it.id == cryptoDto.id},
                    )
                }
            )
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

        val newCachedPoets = cryptoDao.getCryptosByPage(startingIndex, pageSize).map { it.toCrypto() }
        emit(Resource.Success(newCachedPoets))

    }

    override fun getCryptoListByPage(page: Int, pageSize: Int): Flow<Result<List<Crypto>>> = flow {
        delay(2000L)
        val startingIndex = page * pageSize
        val dbSize = cryptoDao.getCryptos().size
        if(dbSize - startingIndex > 0) {
            val finalPageSize = if (dbSize - startingIndex < pageSize) dbSize-startingIndex else pageSize
            val cachedCryptos = cryptoDao.getCryptosByPage(startingIndex, finalPageSize)
                .map { it.toCrypto() }
            emit(Result.success(cachedCryptos))
        } else emit(Result.success(emptyList()))
    }

    override fun updateCryptoPin(cryptoId: Int, isPinned: Boolean): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            cryptoDao.updateCryptoPin(cryptoId, isPinned)
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't update crypto pin."))
        }
    }

    override fun updateCryptoLike(cryptoId: Int, isLiked: Boolean): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            cryptoDao.updateCryptoLike(cryptoId, isLiked)
            emit(Resource.Success(1))
        }catch (e: IOException){
            emit(Resource.Error(message = "Couldn't update crypto pin."))
        }
    }

}