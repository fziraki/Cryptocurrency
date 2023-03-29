package com.example.cryptocurrency.domain.model

import com.example.cryptocurrency.data.local.entity.LikedCryptoEntity
import com.example.cryptocurrency.data.local.entity.PinnedCryptoEntity

data class Crypto(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String,
    var isPinned: Boolean,
    var isLiked: Boolean
){
    val priceInUsdtToShow = String.format("%.1f", priceInUsdt.toDouble())
    val changePercentToShow = String.format("%.1f", changePercent.toDouble())

    fun toPinnedCryptoEntity(): PinnedCryptoEntity {
        return PinnedCryptoEntity(
            id = id,
            name = name,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent,
            isPinned = true,
            isLiked = isLiked
        )

    }

    fun toLikedCryptoEntity(): LikedCryptoEntity {
        return LikedCryptoEntity(
            id = id,
            name = name,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent,
            isPinned = isPinned,
            isLiked = true
        )

    }



}