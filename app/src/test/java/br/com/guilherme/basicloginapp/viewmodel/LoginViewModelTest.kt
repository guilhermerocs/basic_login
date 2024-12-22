package br.com.guilherme.basicloginapp.viewmodel

import br.com.guilherme.basicloginapp.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val repository: LoginRepository = mockk()
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `signInWithEmailPassword() - success`() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val firebaseUser: FirebaseUser = mockk()
        every { firebaseUser.email } returns email
        coEvery { repository.signInWithEmailPassword(email, password) } returns Result.success(
            firebaseUser
        )

        viewModel.signInWithEmailPassword(email, password)

        coVerify { repository.signInWithEmailPassword(email, password) }
        val uiState = viewModel.flow.first()
        assertEquals(true, uiState is UiState.LoginSuccess)
        assertEquals(email, (uiState as UiState.LoginSuccess).model.username)
    }

    @Test
    fun `signInWithEmailPassword() - failure`() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val exception = Exception("Login failed")
        coEvery { repository.signInWithEmailPassword(email, password) } returns Result.failure(
            exception
        )

        viewModel.signInWithEmailPassword(email, password)

        coVerify { repository.signInWithEmailPassword(email, password) }
        val uiState = viewModel.flow.first()
        assertEquals(true, uiState is UiState.Error)
        assertEquals(exception, (uiState as UiState.Error).exception)
    }

    @Test
    fun `signInWithEmailPassword() - invalid email`() = runTest {
        val email = "invalid_email"
        val password = "password123"

        viewModel.signInWithEmailPassword(email, password)

        coVerify(exactly = 0) { repository.signInWithEmailPassword(any(), any()) }
        val uiState = viewModel.flow.first()
        assertEquals(true, uiState is UiState.Error)
        assertEquals(
            "Invalid email or password",
            (uiState as UiState.Error).exception.message
        )
    }

    @Test
    fun `signInWithEmailPassword() - short password`() = runTest {
        val email = "test@email.com"
        val password = "pass"

        viewModel.signInWithEmailPassword(email, password)

        coVerify(exactly = 0) { repository.signInWithEmailPassword(any(), any()) }
        val uiState = viewModel.flow.first()
        assertEquals(true, uiState is UiState.Error)
        assertEquals(
            "Invalid email or password",
            (uiState as UiState.Error).exception.message
        )
    }
}