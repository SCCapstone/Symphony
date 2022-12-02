/*
 * Created by Team Symphony 11/28/22, 5:22 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 5:22 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.data.model.WorkoutRoutine

class ExerciseRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var database: FirebaseFirestore = Firebase.firestore

    /**
     * Add a new Exercise to the database
     */
    fun addExercise(name: String, description: String, id: String) {
        val newExercise = Exercise(name, description, id)
        database.collection(EXERCISE_COLLECTION).document(newExercise.exerciseID).set(newExercise)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document", e)
            }
    }

    /**
     * Retrieve a specific Exercise from the database by searching it's ID
     */
    fun getExercise(exeID: String,_exerciseList: MutableLiveData<Exercise>) {
        Log.d(ContentValues.TAG, "Retrieving Exercise $exeID from Firestore")
        val docRef = database.collection(EXERCISE_COLLECTION).document(exeID)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            _exerciseList.value = documentSnapshot.toObject<Exercise>()
        }
    }

    /**
     * Retrieve a LiveData list of exercises by a search term
     */
    fun getExerciseList(searchTerm: String, _exerciseList: MutableLiveData<ArrayList<Exercise>>) {
        Log.d(TAG, "Accessing database with a search term")
        val exeList = arrayListOf<Exercise>()
        _exerciseList.value = exeList
        if (searchTerm != "") {
            Log.d(TAG, "Fetching exercises that match $searchTerm")
            // Search for exercises that match the given term
            database.collection(EXERCISE_COLLECTION)
                .whereArrayContains(TAGS_FIELD, searchTerm)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val t = document.toObject<Exercise>()
                        exeList.add(t)
                        _exerciseList.value = exeList
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
        else {
            // No search term provided, return ALL exercises
            Log.d(TAG, "Fetching all exercises")
            database.collection(EXERCISE_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val t = document.toObject<Exercise>()
                        exeList.add(t)
                        _exerciseList.value = exeList
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    /**
     * Fetch a LiveData list of Exercises by searching through a Workout's list of Exercises
     */
    fun getExerciseList(workoutExeList: ArrayList<String>, _exerciseList: MutableLiveData<ArrayList<Exercise>>) {
        Log.d(TAG, "Fetching exercises for a Workout")
        val exeList = arrayListOf<Exercise>()
        for (exeID in workoutExeList) {
            database.collection(EXERCISE_COLLECTION).document(exeID)
                .get().addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "Lookup for exercise $exeID successful")
                    documentSnapshot.toObject<Exercise>()?.let {
                        exeList.add(documentSnapshot.toObject<Exercise>()!!)
                        _exerciseList.value = exeList
                    }

                }
        }
    }

    /**
     * Add a new Workout to the database
     */
    fun addWorkout(workout: Workout, workID: String) {
        Log.d(TAG, "Adding new workout")
        database.collection(WORKOUT_COLLECTION).document(workID).set(workout)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document", e)
            }
    }

    /**
     * Add a new Workout to the database
     */
    fun addRoutine(name: String, workoutList: ArrayList<String>) {
        Log.d(TAG, "Adding new workout owned by current user")
        val newRoutine = WorkoutRoutine(name, auth.currentUser!!.uid, workoutList)
        database.collection(ROUTINE_COLLECTION).document().set(newRoutine)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document", e)
            }
    }

    /**
     * Fetch a LiveData list of Workouts by searching for ther Current User's ID
     */
    fun getUserWorkout(_workoutRoutineList: MutableLiveData<ArrayList<WorkoutRoutine>>) {
        Log.d(TAG, "Getting workouts owned by the current user")
        val workList = arrayListOf<WorkoutRoutine>()
        database.collection(WORKOUT_COLLECTION)
            .whereEqualTo("ownerID", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val t = document.toObject<WorkoutRoutine>()
                    workList.add(t)
                    _workoutRoutineList.value = workList
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    /**
     * Fetch the current User's workout routines
     */
    fun getUserRoutines(_workoutRoutineList: MutableLiveData<ArrayList<WorkoutRoutine>>) {
        Log.d(TAG, "Getting routines owned by the current user")
        val routineList = arrayListOf<WorkoutRoutine>()
        database.collection(ROUTINE_COLLECTION)
            .whereEqualTo("ownerID", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val t = document.toObject<WorkoutRoutine>()
                    routineList.add(t)
                    _workoutRoutineList.value = routineList
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    /**
     * Fetch a LiveData list of Exercises by searching through a Workout's list of Exercises
     */
    fun getWorkouts(workoutList: ArrayList<String>, _workoutList: MutableLiveData<ArrayList<Workout>>) {
        Log.d(TAG, "Fetching exercises for a Workout")
        val workList = arrayListOf<Workout>()
        for (workoutID in workoutList) {
            database.collection(WORKOUT_COLLECTION).document(workoutID)
                .get().addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "Lookup for exercise $workoutID successful")
                    documentSnapshot.toObject<Exercise>()?.let {
                        workList.add(documentSnapshot.toObject<Workout>()!!)
                        _workoutList.value = workList
                    }

                }
        }
    }

    companion object {
        const val EXERCISE_COLLECTION = "exercises"
        const val WORKOUT_COLLECTION = "workouts"
        const val ROUTINE_COLLECTION = "routines"
        const val TAGS_FIELD = "tags"
    }
}