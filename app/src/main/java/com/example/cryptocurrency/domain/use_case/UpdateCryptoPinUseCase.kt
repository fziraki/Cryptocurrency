package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCryptoPinUseCase @Inject constructor(
    private val cryptoRepository: CryptoRepository
) {
    operator fun invoke(crypto: Crypto, isPinned: Boolean): Flow<Resource<Int>> {
        return cryptoRepository.updateCrypto(crypto.id, isPinned)
    }
}