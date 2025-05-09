package com.faris0048.asessment2.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "jurnal")
data class JurnalHarian(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val tanggal: String,
    val isi: String,
    val idMood: Int
)

data class JurnalWithMood(
    @Embedded val jurnal: JurnalHarian,

    @Relation(
        parentColumn = "idMood",
        entityColumn = "idMood"
    )
    val mood: Mood
)