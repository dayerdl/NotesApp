package com.task.noteapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "created") val created: String = "",
    @ColumnInfo(name = "edited") val edited: String? = "",
    @ColumnInfo(name = "imageUrl") val image: String? = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}