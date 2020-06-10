package com.cactus.datingucc.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.cactus.datingucc.ForgotPassword

import com.cactus.datingucc.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val button = login_button
        button.setOnClickListener {
            PreformLogin()
        }

        login_toregister.setOnClickListener {
            Log.d("RegisterActivity", "Trys to Close Login Activty" )
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        login_forgotpassword.setOnClickListener {
            var intent2 = Intent(this, ForgotPassword::class.java)
            startActivity(intent2)
        }
    }
    private fun PreformLogin() {
        val email = login_email.text.toString()
        val pass = login_password.text.toString()

        Log.d("RegisterActivity", "Username is:$email")
        Log.d("RegisterActivity", "Password is:$pass")

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is:$email")
        Log.d("RegisterActivity", "Password is:$pass")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("RegisterActivity", "Successfully signed in user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to sign user in: ${it.message}")
                Toast.makeText(this, "Incorrect Password ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
