package com.faris0048.asessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.faris0048.asessment2.model.JurnalHarian
import com.faris0048.asessment2.model.JurnalWithMood
import kotlinx.coroutines.flow.Flow

@Dao
interface JurnalHarianDao {
    @Insert
    suspend fun insert(jurnalHarian: JurnalHarian)

    @Update
    suspend fun update(jurnalHarian: JurnalHarian)

    @Transaction
    @Query("SELECT * FROM jurnal j JOIN mood m ON m.idMood = j.idMood")
    fun getJurnalWithMood(): Flow<List<JurnalWithMood>>

    @Query("SELECT COUNT(*) FROM jurnal")
    suspend fun getJurnalCount(): Int

    @Query("SELECT * FROM jurnal WHERE id = :id")
    suspend fun getJurnalById(id: Long): JurnalHarian?

    @Query("DELETE FROM jurnal WHERE id = :id")
    suspend fun deleteById(id: Long)

}