package com.example.videocallwithwebrtc.UI

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.videocallwithwebrtc.R
import com.example.videocallwithwebrtc.databinding.ActivityMainBinding
import com.example.videocallwithwebrtc.repository.MainRepository
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private  val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var username: String? = null

    @Inject lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

//        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
    }

    private fun init(){
        username = intent.getStringExtra("username")
        if (username == null) finish()

        //1. Observe other users status
        subscriberObservers()
        //2. start foreground service to listen negotiations and calls.
        startMyService()
    }
    private fun subscriberObservers(){
        mainRepository.observeUserStatus {
            Log.d(TAG, "subscriberObservers: $it")
        }
    }

    private fun startMyService(){

    }

}