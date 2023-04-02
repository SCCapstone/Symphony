/*
 *  Created by Team Symphony on 2/26/23, 8:55 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 8:55 PM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.databinding.ActivityPremadeBinding




class PreMade : AppCompatActivity() {
    //private lateinit var premade: premadeActivity
    private lateinit var binding: ActivityPremadeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premade)

        val home = binding.BackButton
        home.setOnClickListener {
            val intent = Intent(this, Workout::class.java)
            startActivity(intent)

        }

        val arms = binding.ArmsButton
        arms.setOnClickListener {
            val intent = Intent(this, Arms::class.java)
            startActivity(intent)

        }
        val legs = binding.LegsButton
        legs.setOnClickListener {
            val intent = Intent(this, Legs::class.java)
            startActivity(intent)
        }
    }
}
class Arms : AppCompatActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arms)
    }

}
class Legs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legs)
    }
}
