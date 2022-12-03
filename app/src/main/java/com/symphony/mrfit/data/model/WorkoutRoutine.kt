/*
 * Created by Team Symphony 12/2/22, 2:36 AM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 2:36 AM
 */

package com.symphony.mrfit.data.model

data class WorkoutRoutine(
    val name: String = "",
    val ownerID: String = "",
    val workoutList: ArrayList<String>? = null,
    val routineID: String = ""
)
