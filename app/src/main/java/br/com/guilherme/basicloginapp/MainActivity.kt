package br.com.guilherme.basicloginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.guilherme.basicloginapp.ui.theme.BasicLoginAppTheme
import br.com.guilherme.basicloginapp.ui.view.LoginView
import br.com.guilherme.basicloginapp.ui.view.Welcome
import br.com.guilherme.basicloginapp.viewmodel.LoginViewModel
import br.com.guilherme.basicloginapp.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicLoginAppTheme {
                val uiState = viewModel.flow.collectAsState()
                val navController = rememberNavController()
                LaunchedEffect(uiState.value) {
                    if (uiState.value is UiState.LoginSuccess) {
                        navController.navigate(WELCOME_PAGE_VIEW) {
                            popUpTo(LOGIN_PAGE_VIEW) { inclusive = true }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = LOGIN_PAGE_VIEW,
                ) {
                    setLoginPageContentView(uiState)
                    setWelcomePageContentView(uiState)
                }
            }
        }
    }

    private fun NavGraphBuilder.setLoginPageContentView(
        uiState: State<UiState>,
    ) {
        composable(route = LOGIN_PAGE_VIEW) {
            LoginView(
                uiState = uiState,
                navigateToHome = { },
                onLoginClick = viewModel::signInWithEmailPassword,
                onTryAgainClick = viewModel::onTryAgain
            )
        }
    }

    private fun NavGraphBuilder.setWelcomePageContentView(
        uiState: State<UiState>,
    ) {
        composable(route = WELCOME_PAGE_VIEW) {
            when (val uiStateValue = uiState.value) {
                is UiState.LoginSuccess -> Welcome(uiStateValue)
                else -> {}
            }
        }
    }
}

const val LOGIN_PAGE_VIEW = "login-page-view"
const val WELCOME_PAGE_VIEW = "welcome-page-view"
