package com.task.noteapp.di

import com.task.noteapp.database.NotesCache
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.viewmodel.AddNotesViewModel
import com.task.noteapp.viewmodel.ListNotesViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {

    single { NotesCache(get()) }
    single { NotesRepository(get()) }

    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }
    single { provideCoroutineContext() }

    viewModel { AddNotesViewModel(get(), get()) }
    viewModel { ListNotesViewModel(get(), get()) }

}