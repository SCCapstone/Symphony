package com.symphony.mrfit.data.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser

    fun fetchCurrentUser(){
        userRepository.getCurrentUser(_loggedInUser)
    }

    fun updateCurrentUser(name: String?, age: Int?, height: Int?, weight: Double?){
        userRepository.updateCurrentUser(_loggedInUser, name, age, height, weight)
    }
}