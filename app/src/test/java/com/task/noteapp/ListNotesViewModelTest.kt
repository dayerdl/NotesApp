package com.task.noteapp

import android.content.Context
import com.task.noteapp.database.Note
import com.task.noteapp.database.NotesCache
import com.task.noteapp.di.appModule
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.viewmodel.ListNotesViewModel
import com.task.noteapp.viewmodel.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.coroutines.CoroutineContext

class ListNotesViewModelTest : KoinTest {

    private var context: Context = mockk(relaxed = true)
    private val coroutineContext: CoroutineContext = TestCoroutineDispatcher()
    private lateinit var viewModel: ListNotesViewModel
    private var cache: NotesCache = mockk(relaxed = true)
    private val repository = NotesRepository(cache)

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
        androidContext(context)
        modules(appModule)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = ListNotesViewModel(repository, coroutineContext)
    }

    @Test
    fun `When getNotes is invoke the correct note is returned`() = runBlocking {
        val flow = flow {
            val note = Note("David", "Lopez Dayer", "4", "12", "image")
            val state = listOf(note)
            emit(state)
        }
        coEvery { repository.getNotes() }.answers { flow }

        val note = viewModel.getNotes().first()
        if(note is State.Loaded) {
            assert(note.notes.first().title == "David")
        }
    }

    @Test
    fun `When the note is deleted the repository is called`() = runBlocking {
        viewModel.onDeleteNote(12)
        coVerify { repository.delete(12) }
    }
}