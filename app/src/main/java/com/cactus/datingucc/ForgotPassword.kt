package com.cactus.datingucc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        val email = forgot_email.text.toString()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener {
            if (it.isSuccessful) {

                // No Error, log succesful email sending and have pop up message for user
                Log.d(
                    "ForgotPasswordActivity",
                    "Successfully sent forgot password email to user:${email}"
                )
                Toast.makeText(
                    this,
                    "Password recovery email successfully sent.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        .addOnFailureListener{
            // error, log + pop up message
            Log.d("ForgotPasswordActivity", "Failed to send recovery email to: ${email}")
            Toast.makeText(
                this,
                "Error: ${it}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
