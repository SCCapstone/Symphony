package com.symphony.mrfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Change this section merge conflict */
        val greeting = findViewById<TextView>(R.id.helloTextView)
        greeting.text = "Hello Nik, again"
        greeting.text = "Hello Esam whats up"
        gretting.text = "Hello Nicole"
        greeting.text = "I am trying again"
        gretting.text = "Hi"
        greeting.text = "Hi Lauren"
        gretting.text = "Issue"
        /* Change this section merge conflict */


    }
}