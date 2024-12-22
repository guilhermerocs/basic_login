package br.com.guilherme.basicloginapp.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthenticationImpl @Inject constructor(private val firebase: Firebase) :
    FirebaseAuthentication {

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        firebase.auth.signInWithEmailAndPassword(email, password).await()
        return firebase.auth.currentUser
    }

    override fun getUser(): FirebaseUser? {
        return firebase.auth.currentUser
    }

}