package com.symphony.mrfit.data.register


import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.LoggedInUser
import com.symphony.mrfit.data.User

/**
 * Class for handling User Authentication through Firebase
 */

class RegisterRepository {

    private lateinit var auth: FirebaseAuth

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    // TODO: Replace with registration
    fun register(activity: android.app.Activity, email: String, password: String): Boolean {
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val temp = auth.currentUser
                    this.user = LoggedInUser(User(temp!!.uid, temp.displayName))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity.baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        return user != null
    }

    fun logout(){
        // TODO: Handle logout
    }

    fun getUsername(): String? {
        return user?.name
    }
}