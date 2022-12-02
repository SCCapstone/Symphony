/*
 * Created by Team Symphony 11/28/22, 5:22 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 5:22 PM
 */

package com.symphony.mrfit.data.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExerciseViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            return ExerciseViewModel(
                exerciseRepository = ExerciseRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}