package com.task.noteapp.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.task.noteapp.view.theme.*

private val LightColors = lightColors(
    surface = White,
    onSurface = Gray800,
    primary = GreenMain,
    onPrimary = Black,
    secondary = GreenSecondary,
    error = ErrorColor
)

val Colors.myExtraColor: Color
    @Composable
    get() = if (isLight) Color.Red else Color.Green

private val DarkColors = darkColors(
    surface = Black,
    onSurface = White,
    primary = Gray900,
    onPrimary = White,
    secondary = Gray500,
    error = ErrorColor
)

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}