package com.faris0048.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faris0048.asessment2.database.JurnalHarianDao
import com.faris0048.asessment2.database.MoodDao
import com.faris0048.asessment2.model.JurnalHarian
import com.faris0048.asessment2.model.Mood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailViewModel(private val jurnalDao: JurnalHarianDao, private val moodDao: MoodDao) : ViewModel() {

    val allMood: Flow<List<Mood>> = moodDao.getAllMood()

    fun insert(judul: String, tanggal: String, isi: String, idMood: Int) {
        val jurnal = JurnalHarian(
            judul = judul,
            tanggal = tanggal,
            isi = isi,
            idMood = idMood
        )

        viewModelScope.launch(Dispatchers.IO) {
            jurnalDao.insert(jurnal)
        }
    }

    suspend fun getJurnal(id: Long): JurnalHarian? {
        return jurnalDao.getJurnalById(id)
    }

    fun update(id: Long, judul: String, tanggal: String, isi: String, idMood: Int) {
        val jurnal = JurnalHarian(
            id = id,
            judul = judul,
            tanggal = tanggal,
            isi = isi,
            idMood = idMood
        )

        viewModelScope.launch(Dispatchers.IO) {
            jurnalDao.update(jurnal)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            jurnalDao.deleteById(id)
        }
    }
}