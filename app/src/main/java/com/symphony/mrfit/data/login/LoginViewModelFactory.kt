/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
 */

package com.symphony.mrfit.data.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}