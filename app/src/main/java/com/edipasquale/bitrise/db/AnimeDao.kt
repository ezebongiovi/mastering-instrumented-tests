package com.edipasquale.bitrise.db

import android.database.sqlite.SQLiteDatabase
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

@Dao
interface AnimeDao {
    @Query("SELECT * FROM recentanimeentity")
    fun getAllRecents(): DataSource.Factory<Int, RecentAnimeEntity>

    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    fun insertRecents(entities: List<RecentAnimeEntity>)

    @Delete
    fun delete(user: RecentAnimeEntity)
}