package com.symphony.mrfit.data

import androidx.lifecycle.MutableLiveData
import com.symphony.mrfit.data.model.User

class UserRepository {

    suspend fun getCurrentUser(): User {
        return User("TEST", "Jane Doe", 99, 162, 54.5)
    }

    fun updateCurrentUser(user: MutableLiveData<User>) {

    }

    fun addNewUser() {

    }

    fun removeUser() {

    }
}