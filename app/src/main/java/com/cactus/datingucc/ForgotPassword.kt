package com.cactus.datingucc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgot_button.setOnClickListener{
            ForgotPassword()
        }
    }
    private fun ForgotPassword() {
        val email = forgot_email.toString()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
    }
}
