package br.com.guilherme.basicloginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.guilherme.basicloginapp.util.isValidEmail
import br.com.guilherme.basicloginapp.repository.LoginRepository
import br.com.guilherme.basicloginapp.viewmodel.uimodel.LoginUiModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _flow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val flow: StateFlow<UiState> = _flow

    fun signInWithEmailPassword(email: String, password: String) {
        if (validateFields(email, password)) {
            viewModelScope.launch {
                onPageLoading()
                repository.signInWithEmailPassword(email, password).fold(
                    onSuccess = { result: FirebaseUser? -> onLoginSucceed(result?.email) },
                    onFailure = { exception: Throwable -> onError(exception) },
                )
            }
        } else {
            onError(Throwable("Invalid email or password"))
        }
    }

    private fun validateFields(email: String, password: String): Boolean {
        return email.isValidEmail() && password.length >= 6
    }

    private fun onPageLoading() {
        _flow.update { UiState.Loading }
    }

    private fun onLoginSucceed(username: String?) {
        if (username != null) {
            _flow.update { UiState.LoginSuccess(LoginUiModel(username)) }
        } else {
            onError(Throwable())
        }
    }

    private fun onError(e: Throwable) {
        _flow.update { UiState.Error(e) }
    }

    fun onTryAgain() {
        _flow.update { UiState.Idle }
    }
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    class Error(val exception: Throwable) : UiState()
    data class LoginSuccess(
        val model: LoginUiModel,
    ) : UiState()
}