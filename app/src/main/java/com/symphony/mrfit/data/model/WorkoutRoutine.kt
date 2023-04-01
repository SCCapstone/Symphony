/*
 *  Created by Team Symphony on 3/30/23, 3:45 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/30/23, 1:57 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout Routine, made of several smaller Workouts
 */

data class WorkoutRoutine(
    val name: String = "",
    val ownerID: String = "",
    val playlist: String? = null,
    val workoutList: ArrayList<String>? = null,
    val routineID: String = ""
)
