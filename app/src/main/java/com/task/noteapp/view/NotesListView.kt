package com.task.noteapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.noteapp.R
import com.task.noteapp.database.Note
import com.task.noteapp.navigation.Navigation
import com.task.noteapp.viewmodel.ListNotesViewModel
import com.task.noteapp.viewmodel.State

@Composable
fun NotesListView(navController: NavController, viewModel: ListNotesViewModel) {
    val state = viewModel.getNotes().collectAsState(initial = State.Loading)
    NotesAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.notes_list_screen_title),
                            color = Color.White
                        )
                    },
                    backgroundColor = colorResource(id = R.color.green_500)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Navigation.AddScreen.name)
                    },
                    backgroundColor = colorResource(id = R.color.green_500),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_icon),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
            },
            content = {

                Surface(modifier = Modifier.padding(24.dp)) {
                    when (state.value) {
                        is State.Empty -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.message_empty_list),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                        is State.Loaded -> {
                            val notes = (state.value as State.Loaded).notes
                            ListOfNotes(notes, navController, viewModel)
                        }
                        else -> {

                        }
                    }
                }

            }
        )
    }
}

@Composable
fun ListOfNotes(notes: List<Note>, navController: NavController, viewModel: ListNotesViewModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(notes) { note ->
            Surface(
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .clickable {
                        navController.navigate(
                            Navigation.AddScreen.name.plus(
                                Navigation.AddScreen.paramId.getQuery(
                                    note.id
                                )
                            )
                        )
                    }
                    .fillMaxWidth()
            ) {
                NoteRowView(note = note, viewModel)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

