/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a User object
 */

data class User(
    val userID: String = "",
    var name: String? = null,
    var age: Int? = null,
    var height: Int? = null,
    var weight: Double? = null,
    var visibility: Int = 0, // 0 = Private, 1 = Friends, 2 = Public
    var goals: ArrayList<Goal>? = null,
    var friends: ArrayList<String>? = null,
    var savedTemplates: ArrayList<Int>? = null,
    var posts: ArrayList<Post>? = null
)


