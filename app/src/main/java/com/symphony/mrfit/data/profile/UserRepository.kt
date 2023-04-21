/*
 *  Created by Team Symphony on 4/19/23, 7:07 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/19/23, 7:07 PM
 */

package com.symphony.mrfit.data.profile

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.data.model.Goal
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.Notification
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore
    private var storage: FirebaseStorage = Firebase.storage

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
        return try {
            val snapshot = docRef.get().await()
            // Something wrong happened when deleting this user, fix it
            if (snapshot.get("userID") == null) {
                LoginRepository().delete()
                LoginRepository().logout()
                User("delete")
            }
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
        newHeight: Double?,
        newWeight: Double?
    ) : User? {
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
    suspend fun removeUser() {
        val user = auth.currentUser!!
        Log.d(TAG, "Removing User ${user.uid} from Firestore")
        try {
            database.collection(USER_COLLECTION).document(user.uid)
                .delete()
                .await()
            Log.d(TAG, "DocumentSnapshot successfully deleted!")
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error deleting document", e)
        }
    }

    /**
     * Add a new Workout History to the user's subcollection
     */
    suspend fun addWorkoutHistory(history: History) {
        val user = auth.currentUser!!
        Log.d(TAG, "Adding to the history of ${user.uid}")
        try {
            val docRef = database.collection(USER_COLLECTION).document(user.uid)
                .collection(HISTORY_COLLECTION).add(history).await()
            docRef.update("historyID", docRef.id).await()
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error writing documents: ", e)
        }
    }

    /**
     * Delete a given history via its ID
     */
    suspend fun deleteWorkoutHistory(historyID: String) {
        val user = auth.currentUser!!
        Log.d(TAG, "Deleting history $historyID belonging to ${user.uid}")
        database.collection(USER_COLLECTION)
            .document(user.uid)
            .collection(HISTORY_COLLECTION)
            .document(historyID)
            .delete()
            .await()
    }

    /**
     * Get a user's complete Workout History
     */
    suspend fun getWorkoutHistory(): ArrayList<History> {
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

    /**
     * Retrieve the User's profile picture
     */
    fun getProfilePicture(): StorageReference {
        val ref = storage.reference
            .child(PROFILE_PICTURE)
            .child(auth.currentUser!!.uid).toString()
        Log.d(TAG, "Getting the user's profile picture $ref")
        return storage.reference
            .child(PROFILE_PICTURE)
            .child(auth.currentUser!!.uid)
    }

    /**
     * Add or change a profile picture to a User's Firebase profile
     */
    suspend fun changeProfilePicture(uri: Uri) {
        Log.d(TAG, "Updating the user's profile picture to $uri")
        val ref = storage.reference.child(PROFILE_PICTURE).child(auth.currentUser!!.uid)
        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse(ref.toString())
        }
        auth.currentUser!!.updateProfile(profileUpdates).await()
        ref.putFile(uri)
    }

    /**
     * Add a new Notification to the user's notification subcollection
     */
    suspend fun addNotification(notification: Notification) {
        val user = auth.currentUser!!
        Log.d(TAG, "Adding to the history of ${user.uid}")
        database.collection(USER_COLLECTION)
            .document(user.uid)
            .collection(NOTIFICATION_COLLECTION)
            .document(notification.date!!.toDate().time.toString())
            .set(notification)
            .await()
    }

    /**
     * Get a user's scheduled and past notifications
     */
    suspend fun getNotifications(): ArrayList<Notification> {
        val user = auth.currentUser!!
        val notificationList = arrayListOf<Notification>()
        Log.d(TAG, "Getting the notifications of ${user.uid}")
        try {
            val result = database.collection(USER_COLLECTION)
                .document(user.uid)
                .collection(NOTIFICATION_COLLECTION)
                .get()
                .await()

            for (document in result) {
                notificationList.add(document.toObject())
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return notificationList
    }

    /**
     * Remove a notification from the user's subcollection
     */
    suspend fun deleteNotification(date: String) {
        val user = auth.currentUser!!
        Log.d(TAG, "Deleting notification with timestamp: $date")
        database.collection(USER_COLLECTION)
            .document(user.uid)
            .collection(NOTIFICATION_COLLECTION)
            .document(date)
            .delete()
            .await()

    }

    /**
     * Add a new Goal to the user's goal subcollection
     */
    suspend fun addGoal(goal: Goal) {
        val user = auth.currentUser!!
        Log.d(TAG, "Adding to the goals of ${user.uid}")
        try {
            val docRef = database.collection(USER_COLLECTION)
                .document(user.uid)
                .collection(GOAL_COLLECTION)
                .add(goal)
                .await()
            docRef.update("goalID", docRef.id).await()
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
        }
    }

    /**
     * Update an existing goal with its id
     */
    suspend fun updateGoal(goal: Goal) {
        val user = auth.currentUser!!
        Log.d(TAG, "Updating goal ${goal.goalID} belonging to ${user.uid}")
        try {
            database.collection(USER_COLLECTION)
                .document(user.uid)
                .collection(GOAL_COLLECTION)
                .document(goal.goalID!!)
                .set(goal)
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
        }
    }

    /**
     * Get a user's scheduled and past goals
     */
    suspend fun getGoals(): ArrayList<Goal> {
        val user = auth.currentUser!!
        val goalList = arrayListOf<Goal>()
        Log.d(TAG, "Getting the goals of ${user.uid}")
        try {
            val result = database.collection(USER_COLLECTION)
                .document(user.uid)
                .collection(GOAL_COLLECTION)
                .get()
                .await()

            for (document in result) {
                goalList.add(document.toObject())
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return goalList
    }

    /**
     * Remove a goal from the user's subcollection
     */
    suspend fun deleteGoal(goalID: String) {
        val user = auth.currentUser!!
        Log.d(TAG, "Deleting goal with ID: $goalID")
        database.collection(USER_COLLECTION)
            .document(user.uid)
            .collection(GOAL_COLLECTION)
            .document(goalID)
            .delete()
            .await()

    }

    companion object {
        const val USER_COLLECTION = "users"
        const val HISTORY_COLLECTION = "_history"
        const val NOTIFICATION_COLLECTION = "_notifications"
        const val GOAL_COLLECTION = "_goals"
        const val PROFILE_PICTURE = "profilePictures/"
    }
}