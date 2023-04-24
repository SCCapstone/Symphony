/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
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
