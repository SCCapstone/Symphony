/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.data.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser

    fun fetchCurrentUser(){
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
}