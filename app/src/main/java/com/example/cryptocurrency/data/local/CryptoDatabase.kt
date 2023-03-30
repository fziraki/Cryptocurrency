package com.example.cryptocurrency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptocurrency.data.local.dao.CryptoDao
import com.example.cryptocurrency.data.local.dao.LikedCryptoDao
import com.example.cryptocurrency.data.local.dao.PinnedCryptoDao
import com.example.cryptocurrency.data.local.entity.CryptoEntity
import com.example.cryptocurrency.data.local.entity.LikedCryptoEntity
import com.example.cryptocurrency.data.local.entity.PinnedCryptoEntity

@Database(
    entities = [CryptoEntity::class, PinnedCryptoEntity::class, LikedCryptoEntity::class],
    version = 1
)
abstract class CryptoDatabase: RoomDatabase() {

    abstract val cryptoDao: CryptoDao
    abstract val pinnedCryptoDao: PinnedCryptoDao
    abstract val likedCryptoDao: LikedCryptoDao

}