package com.symphony.mrfit.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.login.LoginForm
import com.symphony.mrfit.data.login.LoginResult
import com.symphony.mrfit.data.login.RegisterForm
import com.symphony.mrfit.data.model.LoggedInUser
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
}