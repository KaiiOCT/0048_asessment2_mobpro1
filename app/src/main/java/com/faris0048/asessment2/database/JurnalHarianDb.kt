package com.faris0048.asessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faris0048.asessment2.model.JurnalHarian
import com.faris0048.asessment2.model.Mood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Mood::class, JurnalHarian::class], version = 1, exportSchema = false)
abstract class JurnalHarianDb : RoomDatabase() {
    abstract val jurnalHarianDao: JurnalHarianDao
    abstract val moodDao: MoodDao

    companion object {
        @Volatile
        private var INSTANCE: JurnalHarianDb? = null

        fun getInstance(context: Context): JurnalHarianDb {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    JurnalHarianDb::class.java,
                    "jurnal.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val defaultMoods = listOf(
                                    Mood(1, "Senang", "😊"),
                                    Mood(2, "Sedih", "😢"),
                                    Mood(3, "Marah", "😠"),
                                    Mood(4, "Santai", "😌")
                                )
                                getInstance(context).moodDao.insertMood(defaultMoods)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}