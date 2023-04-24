/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
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
