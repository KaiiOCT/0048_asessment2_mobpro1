package com.faris0048.asessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood")
data class Mood (
    @PrimaryKey(autoGenerate = true)
    val idMood: Int = 0,
    val nama: String,
    val emoji: String
)