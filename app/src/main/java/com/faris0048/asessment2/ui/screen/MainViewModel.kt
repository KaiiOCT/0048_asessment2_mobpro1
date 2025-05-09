package com.faris0048.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faris0048.asessment2.database.JurnalHarianDao
import com.faris0048.asessment2.database.MoodDao
import com.faris0048.asessment2.model.JurnalWithMood
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(jurnalDao: JurnalHarianDao, moodDao: MoodDao) : ViewModel() {
    val data: StateFlow<List<JurnalWithMood>> = jurnalDao.getJurnalWithMood().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}
