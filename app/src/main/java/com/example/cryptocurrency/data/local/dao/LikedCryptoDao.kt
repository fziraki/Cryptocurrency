package com.example.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrency.data.local.entity.LikedCryptoEntity

@Dao
interface LikedCryptoDao {

    @Query("SELECT * FROM LikedCryptoEntity")
    suspend fun getLikedCryptos(): List<LikedCryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedCryptos(crypto: LikedCryptoEntity): Long

    @Query("DELETE FROM LikedCryptoEntity WHERE id = :cryptoId")
    suspend fun deleteLikedCryptos(cryptoId: Int): Int

    @Query("UPDATE LikedCryptoEntity SET isPinned=:isPinned WHERE id = :cryptoId")
    suspend fun updateLikedCryptoPinField(cryptoId: Int, isPinned: Boolean): Int
}