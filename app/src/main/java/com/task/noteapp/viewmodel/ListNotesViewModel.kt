package com.task.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.database.Note
import com.task.noteapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository) : ViewModel() {

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
        viewModelScope.launch {
            notesRepository.delete(id)
        }
    }
}

sealed class State {
    object Empty : State()
    data class Loaded(val notes: List<Note>) : State()
    object Loading: State()
}
