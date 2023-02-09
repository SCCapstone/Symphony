/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:02 PM
 */

package com.symphony.mrfit.data.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.data.model.WorkoutRoutine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    private val _workoutIDList = MutableLiveData<ArrayList<String>>()
    val workoutIDList: LiveData<ArrayList<String>> = _workoutIDList

    private val _workoutRoutineList = MutableLiveData<ArrayList<WorkoutRoutine>>()
    val workoutRoutineList: LiveData<ArrayList<WorkoutRoutine>> = _workoutRoutineList

    fun addExercise(name: String, description: String, id: String) {
        exerciseRepository.addExercise(name, description, id)
    }

    fun addWorkout(workout: Workout) : String{
        var newID = ""
        /**
         * TODO: Is it possible to run this is as a proper coroutine?
         */
        runBlocking{
            val job = launch { newID = exerciseRepository.addWorkout(workout) }
            job.join()
        }
        return newID
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch { exerciseRepository.updateWorkout(workout) }
    }

    fun addWorkoutToRoutine(routineID: String?, workoutList: ArrayList<String>) {
        exerciseRepository.addWorkoutToRoutine(routineID, workoutList)
    }

    fun addRoutine(name: String, workoutList: ArrayList<String>) {
        exerciseRepository.addRoutine(name, workoutList)
    }

    fun updateRoutine(name: String, routineID: String) {
        exerciseRepository.updateRoutine(name, routineID)
    }

    fun getExercise(exeID: String) {
        exerciseRepository.getExercise(exeID, _exercise)
    }

    fun getExercisesBySearch(searchTerm: String) {
        exerciseRepository.getExerciseList(searchTerm, _exerciseList)
    }

    fun getWorkouts(workoutList: ArrayList<String>) {
        exerciseRepository.getWorkouts(workoutList, _workoutList)
    }

    fun getUserRoutines() {
        exerciseRepository.getUserRoutines(_workoutRoutineList)
    }
}