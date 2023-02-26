package com.symphony.mrfit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.R
import com.symphony.mrfit.databinding.premadeBinding




class PreMade : AppCompatActivity() {
    private lateinit var premade: premadeActivity
    private lateinit var binding: premadeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premade)

        val arms = binding.ArmsButton
        arms.setOnClickListener {
            val intent = Intent(this, Arms::class.java)
            startActivity(intent)

        }
        val legs = binding.LegsButton
        arms.setOnClickListener {
            val intent = Intent(this, Legs::class.java)
            startActivity(intent)
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
