/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
 */

package com.symphony.mrfit.data.model

import android.media.Image

/**
 * Data class for a Post object
 */

data class Post(
    val postID: Int,
    val title: String,
    val image: Image,
    val caption: String,
    val userID: String
)
