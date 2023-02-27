/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.databinding.ActivityStoreBinding


class StoreActivity : AppCompatActivity() {
    lateinit var workoutTemplate: WorkoutTemplateActivity
    private lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exportButton = binding.ExportButton

        val thing  = "null"

        exportButton.setOnClickListener {
            val db = Firebase.firestore
            db.collection("WorkoutTemplates").document().set(thing)
                .addOnSuccessListener { result ->
                    Log.d(TAG, "Export")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Did not work", exception)
                }
        }
    }

    interface ExportButton
}