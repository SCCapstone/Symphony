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
import com.symphony.mrfit.data.LoggedInUser
import com.symphony.mrfit.data.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

/**
 * Class for handling User Authentication through Firebase
 */

class LoginRepository {

    private lateinit var auth: FirebaseAuth

    // in-memory cache of the loggedInUser object
    var currentUser: LoggedInUser? = null
        private set

    fun login(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<FirebaseUser>) {
        Log.d(ContentValues.TAG, "signingIntoAccount:$email")
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, set the current user
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val firebaseUser = auth.currentUser
                    user.value = firebaseUser
                    currentUser = LoggedInUser(User(firebaseUser!!.uid, firebaseUser.email))
                    successfulLogin(user, firebaseUser)
                } else {
                    // If sign in fails
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun register(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<FirebaseUser>) {
        Log.d(ContentValues.TAG, "makingAccount:$email")
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val firebaseUser = auth.currentUser
                    user.value = firebaseUser
                    currentUser = LoggedInUser(User(firebaseUser!!.uid, firebaseUser.email))
                    makeUsername()
                    successfulLogin(user, firebaseUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun logout(){
        // TODO: Handle logout
    }

    fun getUsername(): String? {
        return currentUser?.name
    }

    // If registering a new user, set a default username
    private fun makeUsername() {
        val delim = currentUser!!.name!!.indexOf('@')
        currentUser!!.name = currentUser!!.name!!.substring(0, delim)
    }

    private fun successfulLogin(user: MutableLiveData<FirebaseUser>, firebaseUser: FirebaseUser) {
        val profileUpdates = userProfileChangeRequest {
            displayName = currentUser!!.name
        }

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "User profile updated.")
                    user.value = firebaseUser
                }
            }

    }
}