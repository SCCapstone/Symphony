/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for keeping track of the currently logged in User
 */

data class LoggedInUser (val user: User){
    val userID = user.userID
    var name = user.name
    var age = user.age
    var weight = user.weight
    var height = user.height
    var visibility = user.visibility // 0 = Private, 1 = Friends, 2 = Public
    var goals = user.goals
    var friends = user.friends
    var savedTemplates = user.savedTemplates
    var posts = user.posts
}
