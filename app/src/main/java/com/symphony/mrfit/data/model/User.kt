/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
 */

package com.symphony.mrfit.data.model

import android.graphics.Bitmap
import android.media.Image

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


