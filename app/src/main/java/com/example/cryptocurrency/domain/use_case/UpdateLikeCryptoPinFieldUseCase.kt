package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.LikedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateLikeCryptoPinFieldUseCase @Inject constructor(
    private val likedCryptoRepository: LikedCryptoRepository
) {
    operator fun invoke(crypto: Crypto, isPinned: Boolean): Flow<Resource<Int>> {
        return likedCryptoRepository.updateLikedCryptoPinField(crypto.id, isPinned)
    }
}