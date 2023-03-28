package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnPinCryptoUseCase @Inject constructor(
    private val pinnedCryptoRepository: PinnedCryptoRepository
) {
    operator fun invoke(cryptoId: Int): Flow<Resource<Int>> {
        return pinnedCryptoRepository.unPinCrypto(cryptoId)
    }
}