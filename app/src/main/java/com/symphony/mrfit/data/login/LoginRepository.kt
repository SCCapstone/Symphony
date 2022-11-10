package com.symphony.mrfit.data.login


import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.User

/**
 * Class for handling User Authentication through Firebase
 */

class LoginRepository {

    private lateinit var auth: FirebaseAuth

    // in-memory cache of the loggedInUser object
    private var currentUser: User? = null

    /**
     * Attempt to login the user though Firebase User Auth
     */
    fun login(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "signingIntoAccount:$email")
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, set the current user
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val firebaseUser = auth.currentUser
                    currentUser = User(firebaseUser!!.uid, firebaseUser.displayName)
                    successfulLogin(user, firebaseUser)
                } else {
                    // If sign in fails, display a message to the user and tell ViewModel why
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                    user.value = User("ERROR", "Thingyboom go pop")
                }
            }
    }

    /**
     * Attempt to register the user though Firebase User Auth
     */
    fun register(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "makingAccount:$email")
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Registration success, populate a new user
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val firebaseUser = auth.currentUser
                    currentUser = User(firebaseUser!!.uid, firebaseUser.email)
                    makeUsername()
                    successfulLogin(user, firebaseUser)
                } else {
                    // If registration fails, display a message to the user and tell ViewModel why
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                    user.value = User( "ERROR", "Thingyboom go pop")
                }
            }
    }

    fun logout(){
        // TODO: Handle logout
    }

    fun getUsername(): String? {
        return currentUser?.name
    }

    /**
     * If registering a new user, set a default username
     */
    private fun makeUsername() {
        val delim = currentUser!!.name!!.indexOf('@')
        currentUser!!.name = currentUser!!.name!!.substring(0, delim)
        Log.w(ContentValues.TAG, "setNameTo${currentUser?.name}")
    }

    private fun addNewUser() {
        // TODO: Add a new user to the database
    }

    /**
     * After a successful registration, update the LiveData
     * TODO: Separate displayName update from LiveData update
     */
    private fun successfulLogin(user: MutableLiveData<User>, firebaseUser: FirebaseUser) {
        val profileUpdates = userProfileChangeRequest {
            displayName = currentUser!!.name
        }

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "User profile updated.")
                    user.value = currentUser
                }
            }

    }
}