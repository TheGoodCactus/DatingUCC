package com.cactus.datingucc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cactus.datingucc.Home.HomeActivity
import com.cactus.datingucc.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefName = "LoggedIn"
        val sharedPref = getSharedPreferences(prefName, 0)
        if (sharedPref.getBoolean(prefName, false)){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            //If someone isn't logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

