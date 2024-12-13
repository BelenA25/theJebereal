package com.jebereal.jeberealapp.di

import com.jebereal.jeberealapp.concertApp.ConcertApi
import com.jebereal.jeberealapp.concertApp.FirebaseConcertApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.jebereal.jeberealapp.data.repository.AuthRepositoryImpl
import com.jebereal.jeberealapp.data.repository.UsersRepositoryImpl

import com.jebereal.jeberealapp.domain.repository.AuthRepository
import com.jebereal.jeberealapp.domain.repository.UsersRepository

import com.jebereal.jeberealapp.domain.use_cases.auth.*
import com.jebereal.jeberealapp.domain.use_cases.users.Create
import com.jebereal.jeberealapp.domain.use_cases.users.GetUserById
import com.jebereal.jeberealapp.domain.use_cases.users.SaveImage
import com.jebereal.jeberealapp.domain.use_cases.users.Update
import com.jebereal.jeberealapp.domain.use_cases.users.UsersUseCases
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.login.jetpackcompose.core.Constants.USERS
import dagger.Binds

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideStorageUsersRef(storage: FirebaseStorage): StorageReference = storage.reference.child(USERS)

    @Provides
    fun provideUsersRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideUsersRepository(impl: UsersRepositoryImpl): UsersRepository = impl

    @Provides
    fun provideAuthUseCases(repository: AuthRepository) = AuthUseCases(
        getCurrentUser = GetCurrentUser(repository),
        login = Login(repository),
        logout = Logout(repository),
        signup = Signup(repository)
    )

    @Provides
    fun provideUsersUseCases(repository: UsersRepository) = UsersUseCases(
        create = Create(repository),
        getUserById = GetUserById(repository),
        update = Update(repository),
        saveImage = SaveImage(repository)
    )

    @Module
    @InstallIn(SingletonComponent::class) // Change from ViewModelComponent to SingletonComponent
    abstract class ConcertApiModule {
        @Binds
        abstract fun bindConcertApi(
            firebaseConcertApi: FirebaseConcertApi
        ): ConcertApi

        companion object {
            @Provides
            @Singleton
            fun provideFirebaseDatabase(): FirebaseDatabase {
                return Firebase.database
            }
        }
    }

}