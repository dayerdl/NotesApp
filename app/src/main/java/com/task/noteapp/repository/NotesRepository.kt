package com.task.noteapp.repository

import com.task.noteapp.database.Note
import com.task.noteapp.database.NotesCache
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val cache: NotesCache) {

    suspend fun addNote(note: Note): Int {
        return cache.addNote(note)
    }

    fun getNotes(): Flow<List<Note>> {
        return cache.getNotes()
    }

    fun getNotesById(id: Int): Flow<Note> {
        return cache.getNotesById(id)
    }

    suspend fun update(
        id: Int,
        title: String,
        description: String,
        edited: String,
        imageUrl: String
    ) {
        cache.updateNoteById(id, title, description, edited, imageUrl)
    }

    suspend fun delete(id: Int){
        cache.deleteNote(id)
    }
}