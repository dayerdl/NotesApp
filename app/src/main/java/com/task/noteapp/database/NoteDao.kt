package com.task.noteapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(noteDao: Note): Long

    @Delete
    suspend fun deleteNote(noteDao: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE notes.id = :id")
    fun getNotesById(id: Int): Flow<Note>

    @Query("UPDATE notes SET title = :title, description = :description, edited = :edited, imageUrl = :imageUrl WHERE id =:id")
    suspend fun updateNote(
        id: Int,
        title: String,
        description: String,
        edited: String,
        imageUrl: String
    )

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)
}