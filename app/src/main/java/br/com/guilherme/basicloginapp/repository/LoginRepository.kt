package br.com.guilherme.basicloginapp.repository

import com.google.firebase.auth.FirebaseUser

interface LoginRepository {

    suspend fun signInWithEmailPassword(email: String, password: String): Result<FirebaseUser?>

    fun getCurrentUser(): Result<FirebaseUser?>

}