package com.example.videocallwithwebrtc.Utils

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application()
//{
//    override fun onCreate() {
//        super.onCreate()
////        FirebaseApp.initializeApp(this)//initlize firebase
//
//        if (FirebaseApp.getApps(this).isEmpty()) {
//            FirebaseApp.initializeApp(this)
//            Log.d("FirebaseInit", "Firebase initialized successfully")
//        } else {
//            Log.d("FirebaseInit", "Firebase already initialized")
//        }
//    }
//}