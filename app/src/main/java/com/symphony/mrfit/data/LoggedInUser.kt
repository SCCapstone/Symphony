package com.symphony.mrfit.data

/**
 * Data class for keeping track of the currently logged in User
 */

data class LoggedInUser(val user: User){
    val userID = user.userID
    val name = user.name
    val age = user.age
    val weight = user.weight
    val height = user.height
    val visibility = user.visibility // 0 = Private, 1 = Friends, 2 = Public
    val goals = user.goals
    val friends = user.friends
    val savedTemplates = user.savedTemplates
    val posts = user.posts
}
