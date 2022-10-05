package com.task.noteapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.task.noteapp.R
import com.task.noteapp.database.Note
import com.task.noteapp.database.NotesCache
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.viewmodel.ListNotesViewModel

@Composable
fun NoteRowView(note: Note, viewModel: ListNotesViewModel) {
    Column {
        if(note.image?.isNotEmpty() == true) {
            GlideImage(
                imageModel = note.image,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
        }
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = note.description, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(25.dp))
            }
            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painterResource(id = R.drawable.icon_bin), "delete",
                contentScale = ContentScale.Fit, modifier =
                Modifier
                    .size(20.dp)
                    .clickable {
                        viewModel.onDeleteNote(note.id)
                    }
            )

        }
        Row(Modifier.align(Alignment.End)) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = stringResource(R.string.created_at), fontSize = 10.sp)
            Text(text = note.created, fontSize = 10.sp)
            Spacer(modifier = Modifier.width(6.dp))
            note.edited?.let {
                if (it.isNotEmpty()) {
                    Text(text = stringResource(R.string.edited_at), fontSize = 10.sp)
                    Text(text = it, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
    }

}

@Preview
@Composable
fun previewNoteRow() {
    val note = Note("Title", "This is the description", "1/01/2021", "10/01/2021")
    val cache = NotesCache(LocalContext.current)
    val repository = NotesRepository(cache)
    val viewModel = ListNotesViewModel(repository)
    NoteRowView(note = note, viewModel = viewModel)
}
