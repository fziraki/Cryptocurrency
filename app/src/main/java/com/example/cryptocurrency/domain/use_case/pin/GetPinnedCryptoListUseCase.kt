package com.example.cryptocurrency.domain.use_case.pin

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPinnedCryptoListUseCase @Inject constructor(
    private val pinnedCryptoRepository: PinnedCryptoRepository
) {
    operator fun invoke(): Flow<Resource<List<Crypto>>> {
        return pinnedCryptoRepository.getPinnedCryptoList()
    }
}