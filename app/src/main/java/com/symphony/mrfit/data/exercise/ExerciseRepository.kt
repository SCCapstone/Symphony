/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:02 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.data.model.WorkoutRoutine
import kotlinx.coroutines.tasks.await

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
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }
    }

    /**
     * Retrieve a specific Exercise from the database by searching it's ID
     */
    suspend fun getExerciseByID(exeID: String) : Exercise? {
        Log.d(TAG, "Retrieving Exercise $exeID from Firestore")
        val docRef = database.collection(EXERCISE_COLLECTION).document(exeID)
        return try {
            val snapshot = docRef.get().await()
            snapshot.toObject<Exercise>()
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error getting document", e)
            null
        }

    }

    /**
     * Retrieve an array list of exercises by a search term
     */
    suspend fun getExerciseList(searchTerm: String) : ArrayList<Exercise>{
        Log.d(TAG, "Accessing database with a search term")
        val exeList = arrayListOf<Exercise>()
        if (searchTerm != "") {
            Log.d(TAG, "Fetching exercises that match $searchTerm")
            // Search for exercises that match the given term
            try {
                val result = database.collection(EXERCISE_COLLECTION)
                .whereArrayContains(TAGS_FIELD, searchTerm)
                .get()
                .await()

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val t = document.toObject<Exercise>()
                    exeList.add(t)
                }
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "Error getting documents: ", e)
            }
        }
        else {
            // No search term provided, return ALL exercises
            Log.d(TAG, "Fetching all exercises")
            try {
                val result = database.collection(EXERCISE_COLLECTION)
                .get()
                .await()

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val t = document.toObject<Exercise>()
                    exeList.add(t)
                }
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "Error getting documents: ", e)
            }
        }
        return exeList
    }

    /**
     * Fetch an array list of Exercises by searching through a Workout's list of Exercises
     */
    fun getExerciseList(workoutExeList: ArrayList<String>) : ArrayList<Exercise> {
        Log.d(TAG, "Fetching exercises for a Workout")
        val exeList = arrayListOf<Exercise>()
        try {
            for (exeID in workoutExeList) {
                database.collection(EXERCISE_COLLECTION).document(exeID)
                    .get().addOnSuccessListener { documentSnapshot ->
                        Log.d(TAG, "Lookup for exercise $exeID successful")
                        documentSnapshot.toObject<Exercise>()?.let {
                            exeList.add(documentSnapshot.toObject<Exercise>()!!)
                        }
                    }
            }
        } catch (e: java.lang.Exception) {
                Log.d(TAG, "Error getting documents: ", e)
        }
        return exeList
    }

    /**
     * Add a new Workout to the database
     */
    suspend fun addWorkout(workout: Workout) : String {
        Log.d(TAG, "Adding new workout")
        return try {
            val docRef = database.collection(WORKOUT_COLLECTION).add(workout).await()
            docRef.update("workoutID", docRef.id)
            Log.d(TAG, "New workout added at ${docRef.id}!")
            docRef.id
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
            ""
        }
    }

    suspend fun updateWorkout(workout: Workout) {
        Log.d(TAG, "Updating workout ${workout.workoutID}")
        workout.workoutID?.let {
            try {
                database.collection(WORKOUT_COLLECTION).document(it).set(workout).await()
                Log.d(TAG, "Successfully updated ${workout.workoutID}!")
            } catch (e: java.lang.Exception) {
                Log.w(TAG, "Error writing document", e)
            }
        }
    }

    /**
     * Add a new Workout to the database
     */
    fun addWorkoutToRoutine(routineID: String?, workoutList: ArrayList<String>) {
        Log.d(TAG, "Adding new workout to routine $routineID")
        if (routineID != null) {
            database.collection(ROUTINE_COLLECTION).document(routineID).update("workoutList", workoutList)
                .addOnSuccessListener {
                    Log.d(TAG, "Routine $routineID updated!")
                }
                .addOnFailureListener {
                        e -> Log.w(TAG, "Error writing document", e)
                }
        }
        else {
            /**
             * TODO: Make a new Routine
             */
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
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }
    }

    /**
     * Fetch the current User's workout routines
     */
    suspend fun getUserRoutines() : ArrayList<WorkoutRoutine> {
        Log.d(TAG, "Getting routines owned by the current user")
        val routineList = arrayListOf<WorkoutRoutine>()
        try {
            val result = database.collection(ROUTINE_COLLECTION)
                .whereEqualTo("ownerID", auth.currentUser?.uid)
                .get()
                .await()

            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
                val t = document.toObject<WorkoutRoutine>()
                val temp = WorkoutRoutine(
                    t.name,
                    t.ownerID,
                    t.workoutList,
                    document.id
                )
                routineList.add(temp)
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return routineList
    }

    /**
     * Return a list of Workouts by searching through a Routine's list of Workouts
     */
    fun getWorkoutList(workoutList: ArrayList<String>) : ArrayList<Workout>{
        Log.d(TAG, "Fetching exercises for a Workout")
        val workList = arrayListOf<Workout>()
        try {
            for (workoutID in workoutList) {
                // If necessary, add an await() to get all workouts/in order
                database.collection(WORKOUT_COLLECTION).document(workoutID)
                    .get().addOnSuccessListener { documentSnapshot ->
                        Log.d(TAG, "Lookup for exercise $workoutID successful")
                        documentSnapshot.toObject<Workout>()?.let {
                            workList.add(documentSnapshot.toObject<Workout>()!!)
                        }
                    }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return workList
    }

    /**
     * Update a Routine's name
     */
    fun updateRoutine(name: String, routineID: String) {
        Log.d(TAG, "Changing $routineID name to $name")
        database.collection(ROUTINE_COLLECTION).document(routineID).update("name", name)
    }

    companion object {
        const val EXERCISE_COLLECTION = "exercises"
        const val WORKOUT_COLLECTION = "workouts"
        const val ROUTINE_COLLECTION = "routines"
        const val TAGS_FIELD = "tags"
    }
}