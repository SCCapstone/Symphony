package com.symphony.mrfit.data

/**
 * Data class for a User object
 */

data class User(
    val userID: String,
    var name: String?,
    var age: Int? = null,
    var weight: Float? = null,
    var height: Float? = null,
    var visibility: Int = 0, // 0 = Private, 1 = Friends, 2 = Public
    var goals: ArrayList<Goal>? = null,
    var friends: ArrayList<String>? = null,
    var savedTemplates: ArrayList<Int>? = null,
    var posts: ArrayList<Post>? = null
)
