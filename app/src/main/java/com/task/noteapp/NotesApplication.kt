package com.task.noteapp

import android.app.Application
import com.task.noteapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class NotesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@NotesApplication)
            modules(appModule)
        }
    }
}