package com.task.noteapp.navigation

object Navigation {
    object ListScreen {
        const val name = "list"
    }
    
    object AddScreen {
        const val name = "add"
        val paramId = Param("id", "?id={id}")
    }

    data class Param(val name: String, val scheme: String){
        fun getQuery(id: Int): Any? {
            return "?$name=${id}"
        }
    }
}