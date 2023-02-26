/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.launch

/**
 * ViewModel for interacting with the User Repository
 */

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser

    private val _workoutHistory = MutableLiveData<ArrayList<History>>()
    val workoutHistory: LiveData<ArrayList<History>> = _workoutHistory

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _loggedInUser.value = userRepository.getCurrentUser()
        }
    }

    fun updateCurrentUser(name: String?, age: Int?, height: Int?, weight: Double?){
        viewModelScope.launch {
            _loggedInUser.value = userRepository.updateCurrentUser(name, age, height, weight)
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
}