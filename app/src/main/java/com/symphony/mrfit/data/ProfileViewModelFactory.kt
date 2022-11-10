package com.symphony.mrfit.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.data.login.LoginViewModel

class ProfileViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                userRepository = UserRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}