package com.symphony.mrfit.data.login

import com.symphony.mrfit.data.LoggedInUser
import com.symphony.mrfit.data.User
import java.io.IOException
import java.util.*

/**
 * Class for handling User Authentication through Firebase
 */

class LoginRepository {
    fun login(email: String, password: String): Boolean {
        // TODO: Add Firebase Authentication
        val fakeUser = User(UUID.randomUUID().toString(), "Jane Doe")
        val fakeLog = LoggedInUser(fakeUser)
        return true
    }

    fun logout(){
        // TODO: Handle logout
    }
}