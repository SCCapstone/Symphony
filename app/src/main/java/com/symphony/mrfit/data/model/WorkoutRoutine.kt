/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout Routine, made of several smaller Workouts
 */

data class WorkoutRoutine(
    val name: String = "",
    val ownerID: String = "",
    val workoutList: ArrayList<String>? = null,
    val routineID: String = ""
)
