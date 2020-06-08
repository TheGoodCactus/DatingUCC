package com.cactus.datingucc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cactus.datingucc.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startregisteractivity.setOnClickListener {
            Log.d("RegisterActivity", "Try to show register Activity" )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
