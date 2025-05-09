package com.faris0048.asessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faris0048.asessment2.R
import com.faris0048.asessment2.model.Mood
import com.faris0048.asessment2.ui.theme.Asessment2Theme
import com.faris0048.asessment2.util.ViewModelFactory

const val KEY_ID_JURNAL = "idJurnal"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }

    val moodList by viewModel.allMood.collectAsState(initial = emptyList())
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(moodList) {
        if (id == null || moodList.isEmpty()) return@LaunchedEffect
        val data = viewModel.getJurnal(id) ?: return@LaunchedEffect
        judul = data.judul
        tanggal = data.tanggal
        isi = data.isi

        selectedMood = moodList.firstOrNull { it.idMood == data.idMood } ?: moodList.getOrNull(0)
    }

    if (selectedMood == null && moodList.isNotEmpty()) {
        selectedMood = moodList[0]
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = colorResource(R.color.white)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(
                            id = if (id == null) R.string.tambah_jurnal else R.string.edit_jurnal
                        )
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(R.color.dark_blue),
                    titleContentColor = colorResource(R.color.white)
                ),
                actions = {
                    IconButton(onClick = {
                        if (judul.isBlank() || tanggal.isBlank() || isi.isBlank()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (selectedMood == null) {
                            Toast.makeText(context, context.getString(R.string.pilih_mood), Toast.LENGTH_LONG)
                                .show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(judul, tanggal, isi, selectedMood!!.idMood)
                        } else {
                            viewModel.update(id, judul, tanggal, isi, selectedMood!!.idMood)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = colorResource(R.color.white)
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                    }
                }
            )
        }
    ) { padding ->
        selectedMood?.let { it ->
            FormJurnalHarian(
                judul = judul,
                onJudulChange = { judul = it },
                tanggal = tanggal,
                onTanggalChange = { tanggal = it },
                isi = isi,
                onIsiChange = { isi = it },
                selectedMood = it,
                onMoodChange = { selectedMood = it },
                moodList = moodList,
                modifier = Modifier.padding(padding)
            )
        }

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false }
            ) {
                showDialog = false
                viewModel.delete(id)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = colorResource(R.color.white)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                })
        }
    }
}

@Composable
fun FormJurnalHarian(
    judul: String, onJudulChange: (String) -> Unit,
    tanggal: String, onTanggalChange: (String) -> Unit,
    isi: String, onIsiChange: (String) -> Unit,
    selectedMood: Mood, onMoodChange: (Mood) -> Unit,
    moodList: List<Mood>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = judul,
            onValueChange = onJudulChange,
            label = { Text(text = stringResource(R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = colorResource(R.color.blue_primary),
                unfocusedIndicatorColor = colorResource(R.color.blue_primary),
                focusedIndicatorColor = colorResource(R.color.blue_primary),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = colorResource(R.color.blue_primary),
            )
        )
        OutlinedTextField(
            value = tanggal,
            onValueChange = onTanggalChange,
            label = { Text(text = stringResource(R.string.tanggal)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = colorResource(R.color.blue_primary),
                unfocusedIndicatorColor = colorResource(R.color.blue_primary),
                focusedIndicatorColor = colorResource(R.color.blue_primary),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = colorResource(R.color.blue_primary),
            )
        )
        OutlinedTextField(
            value = isi,
            onValueChange = onIsiChange,
            label = { Text(text = stringResource(R.string.isi)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = colorResource(R.color.blue_primary),
                unfocusedIndicatorColor = colorResource(R.color.blue_primary),
                focusedIndicatorColor = colorResource(R.color.blue_primary),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = colorResource(R.color.blue_primary),
            )
        )
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            moodList.forEach { mood ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMoodChange(mood) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedMood == mood,
                        onClick = { onMoodChange(mood) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(R.color.blue_primary)
                        )
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "${mood.emoji} ${mood.nama}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Asessment2Theme {
        DetailScreen(rememberNavController())
    }
}

