package com.example.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrency.data.local.entity.PinnedCryptoEntity

@Dao
interface PinnedCryptoDao {

    @Query("SELECT * FROM PinnedCryptoEntity")
    suspend fun getPinnedCryptos(): List<PinnedCryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPinnedCryptos(crypto: PinnedCryptoEntity): Long

    @Query("DELETE FROM PinnedCryptoEntity WHERE id = :cryptoId")
    suspend fun deletePinnedCryptos(cryptoId: Int): Int

}