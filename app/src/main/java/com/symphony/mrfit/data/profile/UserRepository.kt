/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
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
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore

    /**
     * Pull the currently logged in user's profile from Firestore
     */
    suspend fun getCurrentUser() : User? {
        Log.d(ContentValues.TAG, "Retrieving User ${auth.currentUser!!.uid} from Firestore")
        val doc = auth.currentUser!!.uid
        val docRef = database.collection(USER_COLLECTION).document(doc)
        return try { val snapshot = docRef.get().await()
            snapshot.toObject<User>()
        } catch (_: java.lang.Exception) {
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
        Log.d(ContentValues.TAG, "Updating User $uid in Firestore")
        newName?.let {user!!.name= newName}
        newAge?.let {user!!.age = newAge}
        newHeight?.let {user!!.height = newHeight}
        newWeight?.let {user!!.weight = newWeight}

        try { database.collection(USER_COLLECTION).document(uid).set(user!!).await()
            Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
        } catch (e: java.lang.Exception) {
          Log.w(ContentValues.TAG, "Error writing document", e)
        }
        return user
    }

    /**
     * Add a new user to Firestore
     * Use the user's ID as the document or they will be lost to the database forever
     */
    suspend fun addNewUser(user: User) {
        Log.d(ContentValues.TAG, "Adding User to Firestore")
        try { database.collection(USER_COLLECTION).document(user.userID).set(user).await()
            Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
        } catch (e: java.lang.Exception) {
            Log.w(ContentValues.TAG, "Error writing document", e)
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
    suspend fun addWorkoutHistory(history: History) {
        val user = auth.currentUser!!
        Log.d(ContentValues.TAG, "Adding to the history of ${user.uid}")
        database.collection(USER_COLLECTION).document(user.uid)
            .collection(HISTORY_COLLECTION).add(history).await()
    }

    companion object {
        const val USER_COLLECTION = "users"
        const val HISTORY_COLLECTION = "_history"
    }
}