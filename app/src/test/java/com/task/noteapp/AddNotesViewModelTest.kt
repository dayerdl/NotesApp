package com.task.noteapp

import android.content.Context
import com.task.noteapp.database.Note
import com.task.noteapp.database.NotesCache
import com.task.noteapp.di.appModule
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.viewmodel.AddNotesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddNotesViewModelTest : KoinTest {

    private val coroutineContext: CoroutineContext = TestCoroutineDispatcher()
    private lateinit var viewModel: AddNotesViewModel
    private var context: Context = mockk(relaxed = true)
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
        viewModel = AddNotesViewModel(repository, coroutineContext)
    }

    @Test
    fun `When title change the title change its value and display error is set to false`() {
        viewModel.onTitleChanged("something")
        assert(viewModel.title.value == "something")
    }

    @Test
    fun `When onImageUrlChanged the image url value changes`() {
        viewModel.onImageUrlChanged("something")
        assert(viewModel.imageUrl.value == "something")
    }

    @Test
    fun `When onAddButtonClicked is clicked if the title is empty then display error`() {
        viewModel.onAddButtonClicked(10, "", "description", "image")
        assert(viewModel.displayError.value)
    }

    @Test
    fun `When onAddButtonClicked is clicked, title is not empty and the id is -1 then a new note is added`() {
        coEvery { cache.addNote(any()) }.answers { 100 }
        viewModel.onAddButtonClicked(-1, "title", "description", "image")

        assert(!viewModel.displayError.value)
        assert(viewModel.noteId.value == 100)
    }

    @Test
    fun `When onAddButtonClicked is clicked, title is not empty and the id is different than -1 then the note is edited`() {
        coEvery { cache.addNote(any()) }.answers { 100 }
        viewModel.onAddButtonClicked(12, "title", "description", "image")
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        assert(!viewModel.displayError.value)
        coVerify { repository.update(12, "title", "description", currentDate,"image") }
    }

    @Test
    fun `When loading a note with id != -1 the flow data gets updated correclty`(){
        val flow = flow {
            val note = Note("David", "Lopez Dayer", "4", "12", "image")
            emit(note)
        }
        coEvery { repository.getNotesById(12) }.answers { flow }
        viewModel.loadNote(12)
        assert(viewModel.noteId.value == 12)
        assert(viewModel.title.value == "David")
        assert(viewModel.description.value == "Lopez Dayer")
        assert(viewModel.imageUrl.value == "image")
    }

    @Test
    fun `When loading a note with id = -1 the fields are reset`(){
        viewModel.loadNote(-1)
        assert(viewModel.noteId.value == -1)
        assert(viewModel.title.value == "")
        assert(viewModel.description.value == "")
        assert(viewModel.imageUrl.value == "")
    }

}