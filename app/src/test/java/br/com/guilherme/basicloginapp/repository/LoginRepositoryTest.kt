package br.com.guilherme.basicloginapp.repository


import br.com.guilherme.basicloginapp.data.FirebaseAuthentication
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginRepositoryImplTest {

    private val firebaseAuthentication: FirebaseAuthentication = mockk()
    private val repository = LoginRepositoryImpl(firebaseAuthentication)

    @Test
    fun `signInWithEmailPassword() - success`() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val firebaseUser: FirebaseUser = mockk()
        coEvery {
            firebaseAuthentication.signInWithEmailPassword(
                email,
                password
            )
        } returns firebaseUser

        val result = repository.signInWithEmailPassword(email, password)

        coVerify { firebaseAuthentication.signInWithEmailPassword(email, password) }
        assertEquals(true, result.isSuccess)
        assertEquals(firebaseUser, result.getOrNull())
    }

    @Test
    fun `signInWithEmailPassword() - failure`() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val exception = Exception("Login failed")
        coEvery {
            firebaseAuthentication.signInWithEmailPassword(
                email,
                password
            )
        } throws exception

        val result = repository.signInWithEmailPassword(email, password)

        coVerify { firebaseAuthentication.signInWithEmailPassword(email, password) }
        assertEquals(true, result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getCurrentUser() - success`() = runTest {
        val firebaseUser: FirebaseUser = mockk()
        coEvery { firebaseAuthentication.getUser() } returns firebaseUser

        val result = repository.getCurrentUser()

        coVerify { firebaseAuthentication.getUser() }
        assertEquals(true, result.isSuccess)
        assertEquals(firebaseUser, result.getOrNull())
    }

    @Test
    fun `getCurrentUser() - failure`() = runTest {
        val exception = Exception("Get user failed")
        coEvery { firebaseAuthentication.getUser() } throws exception

        val result = repository.getCurrentUser()

        coVerify { firebaseAuthentication.getUser() }
        assertEquals(true, result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}