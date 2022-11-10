package com.symphony.mrfit.data.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.User

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore

    suspend fun getCurrentUser(): User {
        return User("TEST", "Jane Doe", 99, 162, 54.5)
    }

    fun updateCurrentUser(): User {
        return User("TEST2", "Dr. Darling", 999, 9999, 1010.1)
        /*
        if (name != null) { database.child("users").child("name") }
         */
    }

    fun addNewUser(_loggedInUser: MutableLiveData<User>) {
        Log.d(ContentValues.TAG, "Adding User to Firestore")
        val user = _loggedInUser.value
        if (user != null) {
            database.collection("users").add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }
    }

    fun removeUser() {

    }
}