package com.example.videocallwithwebrtc.repository

import com.example.videocallwithwebrtc.FirebaseClient.FirebaseClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseClient: FirebaseClient
){
    fun login(userName:String, password:String,isDone:(Boolean,String?) -> Unit){
        firebaseClient.login(userName,password,isDone)
    }

    fun observeUserStatus(status: (List<Pair<String,String>>) -> Unit){
        firebaseClient.observeUserStatus(status)
    }
}