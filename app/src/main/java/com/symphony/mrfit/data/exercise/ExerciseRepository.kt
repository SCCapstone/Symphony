/*
 *  Created by Team Symphony on 2/26/23, 11:03 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 9:30 AM
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
    suspend fun addExercise(
        name: String,
        description: String,
        tags: ArrayList<String>,
    ): String {
        Log.d(TAG, "Adding new workout")
        val newExercise = Exercise(name, description, tags)
        return try {
            val docRef = database.collection(EXERCISE_COLLECTION).add(newExercise).await()
            docRef.update("exerciseID", docRef.id)
            Log.d(TAG, "New exercise added at ${docRef.id}!")
            docRef.id
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
            ""
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
    suspend fun getExerciseList(workoutExeList: ArrayList<String>) : ArrayList<Exercise> {
        Log.d(TAG, "Fetching exercises for a Workout")
        val exeList = arrayListOf<Exercise>()
        try {
            for (exeID in workoutExeList) {
                val snapshot = database.collection(EXERCISE_COLLECTION).document(exeID)
                .get().await()
                Log.d(TAG, "Lookup for exercise $exeID successful")
                snapshot.toObject<Exercise>()?.let {
                    exeList.add(snapshot.toObject<Exercise>()!!)
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

    /**
     * Update a workout with a new version of itself
     */
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
     * Add or Remove a Workout to the given Routine by replacing its WorkoutList
     * with a modified version
     */
    suspend fun updateRoutineWorkoutList(routineID: String, workoutList: ArrayList<String>) : RoutineListener {
        Log.d(TAG, "Rewriting workout list for $routineID")
        return try {
            database.collection(ROUTINE_COLLECTION).document(routineID)
                .update("workoutList", workoutList).await()
            Log.d(TAG, "Routine $routineID updated!")
            RoutineListener(success = "1")

        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
            RoutineListener(error = 1)
        }
    }

    /**
     * Add a new Workout to the database
     */
    suspend fun addRoutine(name: String, desc: String, workoutList: ArrayList<String>): String {
        Log.d(TAG, "Adding new workout owned by current user")
        val newRoutine = WorkoutRoutine(name, auth.currentUser!!.uid, desc, workoutList)
        return try {
            val docRef = database.collection(ROUTINE_COLLECTION).add(newRoutine).await()
            docRef.update("routineID", docRef.id)
            Log.d(TAG, "New routine added at ${docRef.id}!")
            docRef.id
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error writing document", e)
            ""
        }
    }

    /**
     * Get a routine via it's ID
     */
    suspend fun getRoutine(routineID: String) : WorkoutRoutine? {
        Log.d(TAG, "Retrieving Routine $routineID from Firestore")
        val docRef = database.collection(ROUTINE_COLLECTION).document(routineID)
        return try { val snapshot = docRef.get().await()
            snapshot.toObject<WorkoutRoutine>()
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "Error getting document", e)
            null
        }
    }

    /**
     * Delete a routine from a user's list of saved routines
     */
    suspend fun deleteRoutine(routineID: String) {
        Log.d(TAG, "Removing Routine $routineID from Firestore")
        database.collection(ROUTINE_COLLECTION).document(routineID).delete().await()
    }

    /**
     * Update a Routine's name
     */
    fun updateRoutine(name: String, desc: String? = null, routineID: String) {
        Log.d(TAG, "Changing $routineID name to $name")
        database.collection(ROUTINE_COLLECTION).document(routineID).update("name", name)
        if (desc != null) {
            database.collection(ROUTINE_COLLECTION).document(routineID).update("description", desc)
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
                    t.description,
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
     * Return a list of Workouts by searching on a Routine's ID
     */
    suspend fun getWorkoutList(routineID: String): ArrayList<Workout> {
        return arrayListOf()
    }

    /**
     * Return a list of Workouts by searching through a Routine's list of Workouts
     */
    suspend fun getWorkoutList(workoutList: ArrayList<String>) : ArrayList<Workout>{
        Log.d(TAG, "Fetching exercises for a Workout")
        val workList = arrayListOf<Workout>()
        try {
            for (workoutID in workoutList) {
                // If necessary, add an await() to get all workouts/in order
                val snapshot = database.collection(WORKOUT_COLLECTION).document(workoutID)
                .get().await()
                Log.d(TAG, "Lookup for workout $workoutID successful")
                snapshot.toObject<Workout>()?.let {
                    workList.add(snapshot.toObject<Workout>()!!)
                }
            }

        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error getting documents: ", e)
        }
        return workList
    }

    companion object {
        const val EXERCISE_COLLECTION = "exercises"
        const val WORKOUT_COLLECTION = "workouts"
        const val ROUTINE_COLLECTION = "routines"
        const val TAGS_FIELD = "tags"
    }
}