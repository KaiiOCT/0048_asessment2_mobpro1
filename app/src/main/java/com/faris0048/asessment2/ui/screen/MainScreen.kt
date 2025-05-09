package com.faris0048.asessment2.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faris0048.asessment2.R
import com.faris0048.asessment2.model.JurnalWithMood
import com.faris0048.asessment2.navigation.Screen
import com.faris0048.asessment2.ui.theme.Asessment2Theme
import com.faris0048.asessment2.util.SettingsDataStore
import com.faris0048.asessment2.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showJurnal by dataStore.layoutFlow.collectAsState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(R.color.dark_blue),
                    titleContentColor = colorResource(R.color.white)
                ),
                actions = {
                    IconButton (onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showJurnal)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showJurnal) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showJurnal) R.string.grid
                                else R.string.list
                            ),
                            tint = colorResource(R.color.white)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                },
                containerColor = colorResource(R.color.dark_blue)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_jurnal),
                    tint = colorResource(R.color.white)
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(showJurnal, Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun ScreenContent(showJurnal: Boolean, modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        if (showJurnal) {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),  // Gunakan fillMaxWidth jika memungkinkan
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) { jurnalWithMood ->
                    ListItem(jurnalWithMood = jurnalWithMood) {
                        navController.navigate(Screen.FormUbah.withId(jurnalWithMood.jurnal.id))
                    }
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) { jurnalWithMood ->
                    GridItem(jurnalWithMood = jurnalWithMood) {
                        navController.navigate(Screen.FormUbah.withId(jurnalWithMood.jurnal.id))
                    }
                }
            }
        }
    }
}


@Composable
fun ListItem(jurnalWithMood: JurnalWithMood, onClick: () -> Unit) {
    val jurnalHarian = jurnalWithMood.jurnal

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = jurnalHarian.judul,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = jurnalHarian.tanggal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = jurnalHarian.isi,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${jurnalWithMood.mood.emoji} ${jurnalWithMood.mood.nama}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun GridItem(jurnalWithMood: JurnalWithMood, onClick: () -> Unit) {
    val jurnalHarian = jurnalWithMood.jurnal

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = jurnalHarian.judul,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = jurnalHarian.tanggal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = jurnalHarian.isi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${jurnalWithMood.mood.emoji} ${jurnalWithMood.mood.nama}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asessment2Theme {
        MainScreen(rememberNavController())
    }
}