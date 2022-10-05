package com.task.noteapp.database

import android.content.Context
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesCache @Inject constructor(private val context: Context) {

    suspend fun addNote(note: Note) : Int {
        return NotesDataBase.getInstance(context).noteDao().insertNote(note).toInt()
    }

    fun getNotes(): Flow<List<Note>> {
        return NotesDataBase.getInstance(context).noteDao().getNotes()
    }

    fun getNotesById(id: Int): Flow<Note> {
        return NotesDataBase.getInstance(context).noteDao().getNotesById(id)
    }

    suspend fun updateNoteById(
        id: Int,
        title: String,
        description: String,
        edited: String,
        imageUrl: String
    ) {
        NotesDataBase.getInstance(context).noteDao().updateNote(id, title, description, edited, imageUrl)
    }

    suspend fun deleteNote(id: Int){
        NotesDataBase.getInstance(context).noteDao().deleteById(id)
    }
}