package com.symphony.mrfit.data.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
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
        val docRef = database.collection("users").document(doc)
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
        newName?.let {_loggedInUser.value?.name = newName}
        newAge?.let {_loggedInUser.value?.age = newAge}
        newHeight?.let {_loggedInUser.value?.height = newHeight}
        newWeight?.let {_loggedInUser.value?.weight = newWeight}

        val user = _loggedInUser.value
        if (user != null) {
            database.collection("users").document(uid).set(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(ContentValues.TAG, "Error writing document", e)
                }
        }
    }

    /**
     * Add a new user to Firestore
     * Use the user's ID as the document or they will be lost to the database forever
     */
    fun addNewUser(_loggedInUser: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Adding User to Firestore")
        val user = _loggedInUser.value
        if (user != null) {
            database.collection("users").document(user.userID).set(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(ContentValues.TAG, "Error writing document", e)
                }
        }

    }

    fun removeUser() {
        Log.d(ContentValues.TAG, "Removing User from Firestore")
    }
}