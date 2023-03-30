package com.example.cryptocurrency.domain.use_case.like

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.repository.LikedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnLikeCryptoUseCase @Inject constructor(
    private val likedCryptoRepository: LikedCryptoRepository
) {
    operator fun invoke(cryptoId: Int): Flow<Resource<Int>> {
        return likedCryptoRepository.unLikeCrypto(cryptoId)
    }
}