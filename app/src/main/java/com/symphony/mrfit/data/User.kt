package com.symphony.mrfit.data

/**
 * Data class for a User object
 */

data class User(
    val userID: String,
    val name: String?,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val visibility: Int = 0, // 0 = Private, 1 = Friends, 2 = Public
    val goals: ArrayList<Goal>? = null,
    val friends: ArrayList<String>? = null,
    val savedTemplates: ArrayList<Int>? = null,
    val posts: ArrayList<Post>? = null
)
