/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
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
