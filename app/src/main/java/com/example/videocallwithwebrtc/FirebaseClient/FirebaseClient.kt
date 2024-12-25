package com.example.videocallwithwebrtc.FirebaseClient

import com.example.videocallwithwebrtc.Utils.FirebaseFieldNames.PASSWORD
import com.example.videocallwithwebrtc.Utils.FirebaseFieldNames.STATUS
import com.example.videocallwithwebrtc.Utils.MyEventListener
import com.example.videocallwithwebrtc.Utils.UserStatus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor(
    private val dbRef: DatabaseReference,
    private val gson: Gson
) {

    private var currentUserName: String? = null
    private fun setUserName(userName: String) {
        this.currentUserName = userName
    }

    fun login(userName: String, password: String, done: (Boolean, String?) -> Unit) {
        dbRef.addListenerForSingleValueEvent(object : MyEventListener() {
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
                                done(false, it.message)
                            }
                    }.addOnFailureListener {
                        done(false, it.message)

                    }
                }
            }

//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
        })
    }

    fun observeUserStatus(status: (List<Pair<String, String>>) -> Unit) {
        dbRef.addValueEventListener(object : MyEventListener() {
            override fun onDataChange(snapshot: DataSnapshot) {
                //all user list except current user and map creates list as a key value pair of username and status
                val list = snapshot.children.filter { it.key != currentUserName }.map {
                    it.key!! to it.child(
                        STATUS
                    ).value.toString()
                }
                //passing back with call back function
                status(list)
            }
        }

        )
    }
}