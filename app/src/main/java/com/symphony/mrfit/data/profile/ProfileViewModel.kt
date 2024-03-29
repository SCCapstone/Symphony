/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.symphony.mrfit.data.model.Goal
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

    private val _goals = MutableLiveData<ArrayList<Goal>>()
    val goals: LiveData<ArrayList<Goal>> = _goals

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _loggedInUser.value = userRepository.getCurrentUser()
        }
    }

    fun updateCurrentUser(name: String?, age: Int?, height: Double?, weight: Double?) {
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
        runBlocking {
            val job = launch { userRepository.addWorkoutHistory(history) }
            job.join()
        }
    }

    fun deleteWorkoutFromHistory(historyID: String) {
        viewModelScope.launch {
            userRepository.deleteWorkoutHistory(historyID)
        }
    }

    fun getWorkoutHistory() {
        viewModelScope.launch {
            _workoutHistory.value = userRepository.getWorkoutHistory()
        }
    }

    fun addNotification(notification: Notification) {
        runBlocking {
            val job = launch { userRepository.addNotification(notification) }
            job.join()
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            _notifications.value = userRepository.getNotifications()
        }
    }

    fun deleteNotification(date: String) {
        viewModelScope.launch {
            userRepository.deleteNotification(date)
        }
    }

    fun addGoal(goal: Goal) {
        runBlocking {
            val job = launch { userRepository.addGoal(goal) }
            job.join()
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            userRepository.updateGoal(goal)
        }
    }

    fun getGoals() {
        viewModelScope.launch {
            _goals.value = userRepository.getGoals()
        }
    }

    fun deleteGoal(goalID: String) {
        viewModelScope.launch {
            userRepository.deleteGoal(goalID)
        }
    }
}