package com.task.noteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDataBase: RoomDatabase(){
    companion object {
        private var instance: NotesDataBase? = null
        const val DB_NAME = "notes"

        fun getInstance(applicationContext: Context): NotesDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    NotesDataBase::class.java,
                    DB_NAME
                ).build()
            }
            return instance as NotesDataBase
        }
    }

    abstract fun noteDao(): NoteDao
}