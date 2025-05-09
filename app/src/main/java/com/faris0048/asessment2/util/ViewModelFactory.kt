package com.faris0048.asessment2.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faris0048.asessment2.database.JurnalHarianDb
import com.faris0048.asessment2.ui.screen.DetailViewModel
import com.faris0048.asessment2.ui.screen.MainViewModel

class ViewModelFactory(
    val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val jurnalDb = JurnalHarianDb.getInstance(context)
        val jurnalDao = jurnalDb.jurnalHarianDao
        val moodDao = jurnalDb.moodDao
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(jurnalDao, moodDao) as T
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(jurnalDao, moodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
