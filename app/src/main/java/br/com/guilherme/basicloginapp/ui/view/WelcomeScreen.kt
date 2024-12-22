package br.com.guilherme.basicloginapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.guilherme.basicloginapp.viewmodel.UiState

@Composable
fun Welcome(uiState: UiState.LoginSuccess) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) { Text(text = "Welcome ${uiState.model.username}") }
}