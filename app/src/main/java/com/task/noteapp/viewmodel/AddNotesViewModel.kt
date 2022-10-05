package com.task.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.database.Note
import com.task.noteapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AddNotesViewModel @Inject constructor(
    private val repository: NotesRepository) : ViewModel() {
    val noteId = MutableStateFlow(-1)
    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val displayError = MutableStateFlow(false)
    lateinit var onNoteAddedSuccessfully : () -> Boolean

    fun onTitleChanged(value: String) {
        title.value = value
        displayError.value = false
    }

    fun onImageUrlChanged(url: String) {
        imageUrl.value = url
    }

    fun onAddButtonClicked(id: Int, title: String, description: String, imageUrl: String) {
        if (title.isEmpty()) {
            displayError.value = true
        } else {
            if (id != -1) {
                editNote(id, title, description, imageUrl)
            } else {
                addNote(title, description, imageUrl)
            }
        }
    }

    private fun editNote(id: Int, title: String, description: String, imageUrl: String) {
        viewModelScope.launch {
            val currentDate = getCurrentDateFormatted()
            repository.update(
                id = id,
                title = title,
                description = description,
                edited = currentDate,
                imageUrl = imageUrl
            )
            withContext(Dispatchers.Main) {
                onNoteAddedSuccessfully()
            }
        }
    }

    private fun addNote(title: String, description: String, imageUrl: String) {
        viewModelScope.launch {
            val currentDate = getCurrentDateFormatted()
            val note = Note(
                title = title,
                description = description,
                created = currentDate,
                image = imageUrl
            )
            noteId.value = repository.addNote(note)
            noteId.collect {
                if (it != -1){
                    withContext(Dispatchers.Main) {
                        onNoteAddedSuccessfully()
                    }
                }
            }
        }
    }

    private fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        return sdf.format(Date())
    }

    fun loadNote(id: Int) {
        noteId.value = -1
        if (id == -1) {
            title.value = ""
            description.value = ""
            imageUrl.value = ""
        } else {
            viewModelScope.launch {
                repository.getNotesById(id).filterNotNull().collect { note ->
                    noteId.value = id
                    title.value = note.title
                    description.value = note.description
                    imageUrl.value = note.image.toString()
                }
            }
        }
    }

    fun onDescriptionChanged(value: String) {
        description.value = value
    }
}
