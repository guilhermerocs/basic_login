package br.com.guilherme.basicloginapp.data

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthentication {

    suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser?

    fun getUser(): FirebaseUser?
}