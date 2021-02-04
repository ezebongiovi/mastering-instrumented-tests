package com.edipasquale.bitrise.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

@Database(entities = arrayOf(RecentAnimeEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AnimeDao
}