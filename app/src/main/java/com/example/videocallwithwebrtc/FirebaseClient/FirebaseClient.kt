package com.example.videocallwithwebrtc.FirebaseClient

import com.example.videocallwithwebrtc.Utils.FirebaseFieldNames.PASSWORD
import com.example.videocallwithwebrtc.Utils.FirebaseFieldNames.STATUS
import com.example.videocallwithwebrtc.Utils.UserStatus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val dbRef: DatabaseReference,
    private val gson: Gson
) {

    private var currentUserName: String? = null
    private fun setUserName(userName: String) {
        this.currentUserName = userName
    }

    fun login(userName: String, password: String, done: (Boolean, String?) -> Unit) {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(userName)) {
                    //user exist,its time to check password
                    val dbPassword = snapshot.child(userName).child(PASSWORD).value
                    if (dbPassword == password) {
                        //password is correct sign in
//                        done(true,null)
                        dbRef.child(userName).child(STATUS).setValue(UserStatus.ONLINE)
                            .addOnCompleteListener {
                                setUserName(userName)
                                done(true, null)
                            }.addOnFailureListener {
                            done(false, "${it.message}")
                        }
                    } else {
                        //password is wrong, notify user
//                        dbRef.child(userName).child(STATUS).setValue("offline")
                        done(false, "wrong password")
                    }
                } else {
                    //user not exist, register user
                    dbRef.child(userName).child(PASSWORD).setValue(password).addOnCompleteListener {
                        dbRef.child(userName).child(STATUS).setValue(UserStatus.ONLINE)
                            .addOnCompleteListener {
                                setUserName(userName)
                                done(true, null)
                            }.addOnFailureListener {
                                done(false,it.message)
                        }
                    }.addOnFailureListener {
                        done(false,it.message)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}