package br.com.guilherme.basicloginapp.di

import br.com.guilherme.basicloginapp.data.FirebaseAuthentication
import br.com.guilherme.basicloginapp.data.FirebaseAuthenticationImpl
import br.com.guilherme.basicloginapp.repository.LoginRepository
import br.com.guilherme.basicloginapp.repository.LoginRepositoryImpl
import com.google.firebase.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebase(): Firebase {
        return Firebase
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthenticator(firebase: Firebase): FirebaseAuthentication {
        return FirebaseAuthenticationImpl(firebase = firebase)
    }

    @Singleton
    @Provides
    fun provideRepository(
        firebaseAuthentication: FirebaseAuthentication
    ): LoginRepository {
        return LoginRepositoryImpl(firebaseAuthentication = firebaseAuthentication)
    }
}