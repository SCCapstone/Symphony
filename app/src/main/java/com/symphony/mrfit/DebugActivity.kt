package com.symphony.mrfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.symphony.mrfit.ui.LoginActivity

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
    }

    fun gotoLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        // finish()
    }
}