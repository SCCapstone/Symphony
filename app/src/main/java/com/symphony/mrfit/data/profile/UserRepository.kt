/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.profile

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore

    /**
     * Add a new user to Firestore
     * Use the user's ID as the document or they will be lost to the database forever
     */
    suspend fun addNewUser(user: User) {
        Log.d(TAG, "Adding User to Firestore")
        try { database.collection(USER_COLLECTION).document(user.userID).set(user).await()
            Log.d(TAG, "DocumentSnapshot successfully written!")
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
        }

    }

    /**
     * Pull the currently logged in user's profile from Firestore
     */
    suspend fun getCurrentUser() : User? {
        Log.d(TAG, "Retrieving User ${auth.currentUser!!.uid} from Firestore")
        val doc = auth.currentUser!!.uid
        val docRef = database.collection(USER_COLLECTION).document(doc)
        return try { val snapshot = docRef.get().await()
            snapshot.toObject<User>()
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error getting document", e)
            null
        }
    }

    /**
     * Read data from Edit Profile form and update Firestore accordingly
     */
    suspend fun updateCurrentUser(
    newName: String?,
    newAge: Int?,
    newHeight: Int?,
    newWeight: Double?) : User? {
        val user = getCurrentUser()
        val uid = auth.currentUser!!.uid
        Log.d(TAG, "Updating User $uid in Firestore")
        newName?.let {user!!.name= newName}
        newAge?.let {user!!.age = newAge}
        newHeight?.let {user!!.height = newHeight}
        newWeight?.let {user!!.weight = newWeight}

        try { database.collection(USER_COLLECTION).document(uid).set(user!!).await()
            Log.d(TAG, "DocumentSnapshot successfully written!")
        } catch (e: java.lang.Exception) {
          Log.w(TAG, "Error writing document", e)
        }
        return user
    }

    /**
     * Only called when a user is being deleted, remove their document from Firestore
     */
    fun removeUser() {
        val user = auth.currentUser!!
        Log.d(TAG, "Removing User ${user.uid} from Firestore")
        database.collection(USER_COLLECTION).document(user.uid)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    /**
     * Add a new Workout History to the user's subcollection
     */
    suspend fun addWorkoutHistory(history: History) {
        val user = auth.currentUser!!
        Log.d(TAG, "Adding to the history of ${user.uid}")
        database.collection(USER_COLLECTION).document(user.uid)
            .collection(HISTORY_COLLECTION).add(history).await()
    }

    /**
     * Get a user's complete Workout History
     */
    suspend fun getWorkoutHistory() : ArrayList<History>{
        val user = auth.currentUser!!
        val historyList = arrayListOf<History>()
        Log.d(TAG, "Getting the history of ${user.uid}")
        try {
            val result = database.collection(USER_COLLECTION)
                .document(user.uid)
                .collection(HISTORY_COLLECTION)
                .get()
                .await()

            for (document in result) {
                historyList.add(document.toObject())
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return historyList
    }

    companion object {
        const val USER_COLLECTION = "users"
        const val HISTORY_COLLECTION = "_history"
    }
}