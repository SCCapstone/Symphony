/*
 *  Created by Team Symphony on 2/26/23, 3:04 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 12:05 PM
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

    private val _routine = MutableLiveData<WorkoutRoutine>()
    val routine: LiveData<WorkoutRoutine> = _routine

    private val _routineListener = MutableLiveData<RoutineListener>()
    val routineListener: LiveData<RoutineListener> = _routineListener

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

    fun updateRoutineWorkoutList(routineID: String, workoutList: ArrayList<String>) {
        viewModelScope.launch {
            _routineListener.value = exerciseRepository.updateRoutineWorkoutList(routineID, workoutList)
        }
    }

    fun addRoutine(name: String, desc: String, workoutList: ArrayList<String>): String {
        var newID = ""
        runBlocking {
            val job = launch { newID = exerciseRepository.addRoutine(name, desc, workoutList) }
            job.join()
        }
        return newID
    }

    fun getRoutine(routineID: String){
        viewModelScope.launch {
            _routine.value = exerciseRepository.getRoutine(routineID)
        }
    }

    fun deleteRoutine(routineID: String) {
        viewModelScope.launch {
            exerciseRepository.deleteRoutine(routineID)
        }
    }

    fun updateRoutine(name: String, routineID: String) {
        viewModelScope.launch {
            exerciseRepository.updateRoutine(name, routineID = routineID)
        }
    }

    fun updateRoutine(name: String, desc: String, routineID: String) {
        viewModelScope.launch {
            exerciseRepository.updateRoutine(name, desc, routineID)
        }
    }

    fun addExercise(
        name: String,
        description: String,
        tags: ArrayList<String>,
    ): String {
        var newID = ""
        runBlocking {
            val job = launch { newID = exerciseRepository.addExercise(name, description, tags) }
            job.join()
        }
        return newID
    }

    fun getExercise(exeID: String) {
        viewModelScope.launch {
            _exercise.value = exerciseRepository.getExerciseByID(exeID)
        }
    }

    fun getExerciseByWorkout(workoutID: String) {
        viewModelScope.launch {
            //workout = exerciseRepository.getExercise(workoutID)
        }
    }

    fun getExercisesBySearch(searchTerm: String) {
        viewModelScope.launch {
            _exerciseList.value = exerciseRepository.getExerciseList(searchTerm)
        }
    }

    fun getWorkouts(workoutList: ArrayList<String>) {
        viewModelScope.launch {
            _workoutList.value = exerciseRepository.getWorkoutList(workoutList)
        }
    }

    fun getWorkouts(routineID: String) {
        viewModelScope.launch {
            _workoutList.value = exerciseRepository.getWorkoutList(routineID)
        }
    }

    fun getUserRoutines() {
        viewModelScope.launch {
            _workoutRoutineList.value = exerciseRepository.getUserRoutines()
        }
    }
}