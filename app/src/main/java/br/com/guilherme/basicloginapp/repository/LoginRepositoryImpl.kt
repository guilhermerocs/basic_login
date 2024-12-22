package br.com.guilherme.basicloginapp.repository

import br.com.guilherme.basicloginapp.data.FirebaseAuthentication
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
) : LoginRepository {

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return try {
            Result.success(firebaseAuthentication.signInWithEmailPassword(email, password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Result<FirebaseUser?> {
        return try {
            Result.success(firebaseAuthentication.getUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}