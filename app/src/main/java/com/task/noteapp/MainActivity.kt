package com.task.noteapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var coroutineContext: CoroutineContext
    @Inject lateinit var repository: NotesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listViewModel: ListNotesViewModel by viewModels()

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
                    val viewModel = hiltViewModel<AddNotesViewModel>()
                    AddNoteView(viewModel, navController)
                }
            }
        }
    }
}