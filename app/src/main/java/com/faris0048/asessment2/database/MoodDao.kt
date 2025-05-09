package com.faris0048.asessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faris0048.asessment2.model.Mood
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(moods: List<Mood>)

    @Query("SELECT * FROM mood")
    fun getAllMood(): Flow<List<Mood>>
}
