/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
 */

package com.symphony.mrfit.data.login

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.data.profile.UserRepository

/**
 * Class for handling User Authentication through Firebase
 */

class LoginRepository {

    // Initialize Auth tools and database repository
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private var userRepository: UserRepository = UserRepository()

    // in-memory cache of the loggedInUser object
    private var currentUser: User? = null

    /**
     * Attempt to login the user through Google Auth
     */
    fun googleLogin(activity: android.app.Activity) {
    }


    /**
     * Attempt to login the user though Firebase User Auth
     */
    fun firebaseLogin(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Signing in to account: $email")
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, set the current user
                    Log.d(ContentValues.TAG, "Sign in with email: success")
                    val firebaseUser = firebaseAuth.currentUser
                    currentUser = User(firebaseUser!!.uid, firebaseUser.displayName)
                    successfulLogin(user)
                } else {
                    // If sign in fails, display a message to the user and tell ViewModel why
                    Log.w(ContentValues.TAG, "Sign in with email: failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                    user.value = User("ERROR", "Authentication rejected Login")
                }
            }
    }

    /**
     * Attempt to register the user though Firebase User Auth
     */
    fun register(activity: android.app.Activity, email: String, password: String, user: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Making account: $email")
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Registration success, populate a new user
                    Log.d(ContentValues.TAG, "Create user with email: success")
                    val firebaseUser = firebaseAuth.currentUser
                    currentUser = User(firebaseUser!!.uid, firebaseUser.email)
                    makeUsername()
                    successfulRegistration(user, firebaseUser)
                } else {
                    // If registration fails, display a message to the user and tell ViewModel why
                    Log.w(ContentValues.TAG, "Create user with email: failure", task.exception)
                    Toast.makeText(
                        activity.baseContext,
                        "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                    user.value = User( "ERROR", "Authentication rejected Register")
                }
            }
    }

    /**
     * Send a password reset to a given email
     */
    fun passwordReset(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Email sent.")
                }
            }
    }

    /**
     * Logout the user from Firebase Auth
     */
    fun logout(){
        Log.d(ContentValues.TAG, "Attempting to log out user")
        firebaseAuth.signOut()
    }

    /**
     * Say goodbye to our lovely user. Remove them from Auth and Database
     */
    fun delete(){
        val user = firebaseAuth.currentUser!!

        userRepository.removeUser()

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "User account deleted.")
                }
            }
    }

    /**
     * After a successful registration, update the LiveData
     */
    private fun successfulLogin(user: MutableLiveData<User>) {
        userRepository.getCurrentUser(user)
    }

    /**
     * After a successful registration, update the LiveData
     * TODO: Separate displayName update from LiveData update
     */
    private fun successfulRegistration(user: MutableLiveData<User>, firebaseUser: FirebaseUser) {
        val profileUpdates = userProfileChangeRequest {
            displayName = currentUser!!.name
        }

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "User profile updated.")
                    user.value = currentUser
                    addNewUser(user)
                }
            }

    }

    /**
     * If registering a new user, set a default username
     */
    private fun makeUsername() {
        val delim = currentUser!!.name!!.indexOf('@')
        currentUser!!.name = currentUser!!.name!!.substring(0, delim)
        Log.w(ContentValues.TAG, "New user's name is ${currentUser?.name}")
    }

    private fun addNewUser(user: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Telling repo to add ${currentUser?.name} to the database")
        userRepository.addNewUser(user)
    }
}