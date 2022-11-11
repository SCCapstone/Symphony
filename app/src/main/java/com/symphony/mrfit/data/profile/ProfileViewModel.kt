/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:37 PM
 */

package com.symphony.mrfit.data.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser

    fun fetchCurrentUser(){
        userRepository.getCurrentUser(_loggedInUser)
    }

    fun updateCurrentUser(name: String?, age: Int?, height: Int?, weight: Double?){
        viewModelScope.launch {
            userRepository.getCurrentUser(_loggedInUser) // Remove this later, only for debugging
            delay(1000)
            userRepository.updateCurrentUser(_loggedInUser, name, age, height, weight)
        }
    }
}