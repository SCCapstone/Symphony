/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 6:37 PM
 */

package com.symphony.mrfit.data.model

data class WorkoutRoutine(
    val name: String = "",
    val ownerID: String = "",
    val workoutList: ArrayList<String>? = null,
    val routineID: String = ""
)
