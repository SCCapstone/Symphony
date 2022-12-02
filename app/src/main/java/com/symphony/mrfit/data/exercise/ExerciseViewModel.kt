/*
 * Created by Team Symphony 11/28/22, 5:19 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 5:19 PM
 */

package com.symphony.mrfit.data.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.data.model.Workout

/**
 * Class for talking between any UI and the Exercise Repository
 * NOTE: View should never deal with data directly, only through LiveData objects
 */

class ExerciseViewModel(private val exerciseRepository: ExerciseRepository): ViewModel() {


    private val _exercise = MutableLiveData<Exercise>()
    val exercise: LiveData<Exercise> = _exercise

    private val _exerciseList = MutableLiveData<ArrayList<Exercise>>()
    val exerciseList: LiveData<ArrayList<Exercise>> = _exerciseList

    private val _workoutList = MutableLiveData<ArrayList<Workout>>()
    val workoutList: LiveData<ArrayList<Workout>> = _workoutList

    fun addExercise(name: String, description: String, id: String) {
        exerciseRepository.addExercise(name, description, id)
    }

    fun getExercise(exeID: String) {
        exerciseRepository.getExercise(exeID, _exercise)
    }

    fun getExercisesBySearch(searchTerm: String) {
        exerciseRepository.getExerciseList(searchTerm, _exerciseList)
    }

    fun getExercisesByWorkout(exeList: ArrayList<String>) {
        exerciseRepository.getExerciseList(exeList, _exerciseList)
    }

    fun addWorkout(name: String, exeList: ArrayList<String>) {
        exerciseRepository.addWorkout(name, exeList)
    }

    fun getUserWorkouts() {
        exerciseRepository.getUserWorkout(_workoutList)
    }
}