package com.task.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.database.Note
import com.task.noteapp.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListNotesViewModel(
    private val notesRepository: NotesRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    fun getNotes(): Flow<State> {
        return flow {
            notesRepository.getNotes().collect { notes ->
                if(notes.isEmpty()){
                    emit(State.Empty)
                } else {
                    emit(State.Loaded(notes))
                }
            }
        }
    }

    fun onDeleteNote(id: Int) {
        viewModelScope.launch(coroutineContext) {
            notesRepository.delete(id)
        }
    }
}

sealed class State {
    object Empty : State()
    data class Loaded(val notes: List<Note>) : State()
    object Loading: State()
}
