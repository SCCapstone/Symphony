/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:37 PM
 */

package com.symphony.mrfit.data.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.User

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore

    /**
     * Pull the currently logged in user's profile from Firestore
     */
    fun getCurrentUser(_loggedInUser: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Retrieving User ${auth.currentUser!!.uid} from Firestore")
        val doc = auth.currentUser!!.uid
        val docRef = database.collection(USER_COLLECTION).document(doc)
        docRef.get().addOnSuccessListener { documentSnapshot ->
                _loggedInUser.value = documentSnapshot.toObject<User>()
            }
    }

    /**
     * Read data from Edit Profile form and update Firestore accordingly
     */
    fun updateCurrentUser(
    _loggedInUser: MutableLiveData<User>,
    newName: String?,
    newAge: Int?,
    newHeight: Int?,
    newWeight: Double?) {
        val uid = auth.currentUser!!.uid
        Log.d(ContentValues.TAG, "Updating User $uid in Firestore")
        newName?.let {_loggedInUser.value?.name= newName}
        newAge?.let {_loggedInUser.value?.age = newAge}
        newHeight?.let {_loggedInUser.value?.height = newHeight}
        newWeight?.let {_loggedInUser.value?.weight = newWeight}

        val user = _loggedInUser.value
        if (user != null) {
            database.collection(USER_COLLECTION).document(uid).set(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(ContentValues.TAG, "Error writing document", e)
                }
        }
        _loggedInUser.value = user
    }

    /**
     * Add a new user to Firestore
     * Use the user's ID as the document or they will be lost to the database forever
     */
    fun addNewUser(_loggedInUser: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Adding User to Firestore")
        val user = _loggedInUser.value
        if (user != null) {
            database.collection(USER_COLLECTION).document(user.userID).set(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(ContentValues.TAG, "Error writing document", e)
                }
        }

    }

    /**
     * Only called when a user is being deleted, remove their document from Firestore
     */
    fun removeUser() {
        val user = auth.currentUser!!
        Log.d(ContentValues.TAG, "Removing User ${user.uid} from Firestore")
        database.collection(USER_COLLECTION).document(user.uid)
            .delete()
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
    }

    /**
     * Add a new Workout History to the user's subcollection
     */
    fun addWorkoutHistory() {
        val DELETE_ME = ""
        val user = auth.currentUser!!
        Log.d(ContentValues.TAG, "Adding to the history of ${user.uid}")
        database.collection(USER_COLLECTION).document(user.uid)
            .collection(HISTORY_COLLECTION).add(DELETE_ME)
    }

    companion object {
        const val USER_COLLECTION = "users"
        const val HISTORY_COLLECTION = "_history"
    }
}