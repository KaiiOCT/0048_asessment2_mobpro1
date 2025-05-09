package com.faris0048.asessment2.navigation

import com.faris0048.asessment2.ui.screen.KEY_ID_JURNAL

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_JURNAL}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}