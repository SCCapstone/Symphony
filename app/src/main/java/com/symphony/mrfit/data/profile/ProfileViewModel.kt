/*
 *  Created by Team Symphony on 4/1/23, 12:26 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 12:07 AM
 */

package com.symphony.mrfit.data.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.Notification
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel for interacting with the User Repository
 */

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser

    private val _workoutHistory = MutableLiveData<ArrayList<History>>()
    val workoutHistory: LiveData<ArrayList<History>> = _workoutHistory

    private val _notifications = MutableLiveData<ArrayList<Notification>>()
    val notifications: LiveData<ArrayList<Notification>> = _notifications

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _loggedInUser.value = userRepository.getCurrentUser()
        }
    }

    fun updateCurrentUser(name: String?, age: Int?, height: Int?, weight: Double?) {
        viewModelScope.launch {
            _loggedInUser.value = userRepository.updateCurrentUser(name, age, height, weight)
        }
    }

    fun getProfilePicture(): StorageReference? {
        var ref: StorageReference? = null
        runBlocking {
            val job = launch { ref = userRepository.getProfilePicture() }
            job.join()
        }
        return ref
    }

    fun changeProfilePicture(uri: Uri) {
        viewModelScope.launch {
            userRepository.changeProfilePicture(uri)
        }
    }

    fun addWorkoutToHistory(history: History) {
        viewModelScope.launch {
            userRepository.addWorkoutHistory(history)
        }
    }

    fun getWorkoutHistory() {
        viewModelScope.launch {
            _workoutHistory.value = userRepository.getWorkoutHistory()
        }
    }

    fun addNotification(notification: Notification) {
        viewModelScope.launch {
            userRepository.addNotification(notification)
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            _notifications.value = userRepository.getNotifications()
        }
    }

    fun deleteNotifcation() {
        viewModelScope.launch {
            userRepository.deleteNotification()
        }
    }
}