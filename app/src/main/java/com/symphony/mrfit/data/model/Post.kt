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
