package com.task.noteapp.view

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.task.noteapp.R
import com.task.noteapp.database.NotesCache
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.viewmodel.AddNotesViewModel

@Composable
fun AddNoteView(viewModel: AddNotesViewModel, navController: NavController) {
    viewModel.onNoteAddedSuccessfully = navController::navigateUp
    NotesAppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            Divider(Modifier.size(0.dp, 16.dp))
            OutlinedTextField(
                value = viewModel.title.collectAsState().value,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = { Text(stringResource(R.string.place_holder_title)) },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Divider(Modifier.size(0.dp, 16.dp))
            OutlinedTextField(
                value = viewModel.description.collectAsState().value,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text(stringResource(R.string.place_holder_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Divider(Modifier.size(0.dp, 16.dp))
            OutlinedTextField(
                value = viewModel.imageUrl.collectAsState().value,
                onValueChange = { viewModel.onImageUrlChanged(it) },
                label = { Text(stringResource(R.string.image_url)) },
                maxLines = 1, modifier = Modifier.fillMaxWidth()
            )
            Divider(Modifier.size(0.dp, 10.dp))

            AnimatedVisibility(visible = viewModel.displayError.collectAsState().value) {
                Text(stringResource(R.string.message_add_note_error), color = Color.Red)
            }

            Divider(Modifier.size(0.dp, 16.dp))
            Button(
                onClick = {
                    viewModel.onAddButtonClicked(
                        viewModel.noteId.value,
                        viewModel.title.value,
                        viewModel.description.value,
                        viewModel.imageUrl.value
                    )
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = colorResource(id = R.color.green_500),
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.save_button_text))
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddNote() {
    val coroutineScope = rememberCoroutineScope()
    val cache = NotesCache(LocalContext.current)
    val repo = NotesRepository(cache)
    val navController = rememberNavController()
    val viewModel = AddNotesViewModel(repo, coroutineScope.coroutineContext)
    AddNoteView(viewModel, navController)
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddNoteDarkMode() {
    val coroutineScope = rememberCoroutineScope()
    val cache = NotesCache(LocalContext.current)
    val repo = NotesRepository(cache)
    val navController = rememberNavController()
    val viewModel = AddNotesViewModel(repo, coroutineScope.coroutineContext)
    AddNoteView(viewModel, navController)
}

