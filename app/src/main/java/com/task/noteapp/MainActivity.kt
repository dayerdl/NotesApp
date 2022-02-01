package com.task.noteapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.task.noteapp.navigation.Navigation
import com.task.noteapp.repository.NotesRepository
import com.task.noteapp.view.AddNoteView
import com.task.noteapp.view.NotesListView
import com.task.noteapp.viewmodel.AddNotesViewModel
import com.task.noteapp.viewmodel.ListNotesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    private val coroutineContext: CoroutineContext by inject()
    private val repository: NotesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listViewModel: ListNotesViewModel by viewModel()

        setContent {
            val addNoteScreenRoute =
                Navigation.AddScreen.name.plus(Navigation.AddScreen.paramId.scheme)
            val navController = rememberNavController()

            NavHost(navController, startDestination = Navigation.ListScreen.name) {
                composable(Navigation.ListScreen.name) {
                    NotesListView(navController, listViewModel)
                }
                composable(
                    addNoteScreenRoute,
                    arguments = listOf(navArgument(Navigation.AddScreen.paramId.name) {
                        defaultValue = -1; type = NavType.IntType
                    })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt(Navigation.AddScreen.paramId.name) ?: -1
                    val viewModel: AddNotesViewModel = viewModel(
                        factory = AddViewModelFactory(
                            repository = repository,
                            context = coroutineContext,
                            id = id,
                        )
                    )
                    AddNoteView(viewModel, navController)
                }
            }
        }
    }
}

class AddViewModelFactory(
    private val repository: NotesRepository,
    private val context: CoroutineContext,
    private val id: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddNotesViewModel(repository = repository, ioDispatcher = context).apply {
            loadNote(id)
        } as T
    }
}
