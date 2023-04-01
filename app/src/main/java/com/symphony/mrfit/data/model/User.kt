/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
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


