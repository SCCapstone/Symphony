/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.data.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.data.profile.UserRepository
import kotlinx.coroutines.tasks.await

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
    fun googleLogin(activity: Activity) {
    }


    /**
     * Attempt to login the user though Firebase User Auth
     */
    suspend fun firebaseLogin(activity: Activity, email: String, password: String) : LoginResult {
        Log.d(TAG, "Signing in to account: $email")
        return try { firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // Sign in success, set the current user
            Log.d(TAG, "Sign in with email: success")
            val firebaseUser = firebaseAuth.currentUser
            LoginResult(success = firebaseUser!!.displayName)
        } catch (e: java.lang.Exception){
            // If sign in failed, alert user
            Log.w(TAG, "Sign in with email: failure", e)
            LoginResult(error = 1)
        }

    }

    /**
     * Attempt to register the user though Firebase User Auth
     */
    suspend fun firebaseRegister(activity: Activity, email: String, password: String) : LoginResult {
        Log.d(TAG, "Making account: $email")
        return try { firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            // Registration success, populate a new user
            Log.d(TAG, "Create user with email: success")
            val firebaseUser = firebaseAuth.currentUser
            currentUser = User(firebaseUser!!.uid, firebaseUser.email)
            makeUsername()
            successfulRegistration(currentUser!!, firebaseUser)
            LoginResult(success = currentUser!!.name)
        } catch (e: java.lang.Exception) {
            // If registration fails, display a message to the user and tell ViewModel why
            Log.w(TAG, "Create user with email: failure", e)
            LoginResult(error = 1)
        }
    }

    /**
     * Send a password reset to a given email
     */
    fun passwordReset(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    /**
     * Logout the user from Firebase Auth
     */
    fun logout(){
        Log.d(TAG, "Attempting to log out user")
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
                    Log.d(TAG, "User account deleted.")
                }
            }
    }

    /**
     * After a successful registration, update the LiveData
     * TODO: Separate displayName update from LiveData update
     */
    private suspend fun successfulRegistration(user: User, firebaseUser: FirebaseUser) {
        val profileUpdates = userProfileChangeRequest {
            displayName = currentUser!!.name
        }

        try { firebaseUser.updateProfile(profileUpdates).await()
             Log.d(TAG, "User profile updated.")
             addNewUser(user)
        } catch (_: java.lang.Exception) {

        }

    }

    /**
     * If registering a new user, set a default username
     */
    private fun makeUsername() {
        val delim = currentUser!!.name!!.indexOf('@')
        currentUser!!.name = currentUser!!.name!!.substring(0, delim)
        Log.w(TAG, "New user's name is ${currentUser?.name}")
    }

    private suspend fun addNewUser(user: User) {
        Log.d(TAG, "Telling repo to add ${currentUser?.name} to the database")
        userRepository.addNewUser(user)
    }


    fun firebaseLoginTest(email: String, password: String) : Boolean {
        var success  = false
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            success = task.isSuccessful
        }
        return success
    }
}