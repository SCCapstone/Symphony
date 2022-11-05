package com.symphony.mrfit.data.login

import com.symphony.mrfit.data.LoggedInUser
import com.symphony.mrfit.data.User
import java.io.IOException
import java.util.*

/**
 * Class for handling User Authentication through Firebase
 */

class LoginRepository {
    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    fun login(email: String, password: String): Boolean {
        // TODO: Add Firebase Authentication
        val fakeUser = User(UUID.randomUUID().toString(), "Jane Doe")
        this.user = LoggedInUser(fakeUser)
        return true
    }

    fun logout(){
        // TODO: Handle logout
    }

    fun getUsername(): String {
        return user!!.name
    }
}