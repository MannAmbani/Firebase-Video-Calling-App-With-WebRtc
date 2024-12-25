package com.example.videocallwithwebrtc.Utils

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

class AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideDatabaseInstance():FirebaseDatabase = FirebaseDatabase.getInstance() //Firebase.database

    @Provides
    fun provideDatabaseReference(database: FirebaseDatabase) = database.reference
}