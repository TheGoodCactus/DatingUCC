package com.cactus.datingucc.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.cactus.datingucc.ForgotPassword
import com.cactus.datingucc.MainActivity

import com.cactus.datingucc.R
import com.cactus.datingucc.RegisterActivity2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
            val intent2 = Intent(this, ForgotPassword::class.java)
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
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users").child("${currentFirebaseUser?.uid}").child("registered")
                // Read from the database
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val value = dataSnapshot.value
                        Log.d("TAG", "Value is: $value")
                        if (value == false){
                            val intent = Intent(this@LoginActivity, RegisterActivity2::class.java)
                            startActivity(intent)
                        }
                        else {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.d("TAG", "Failed to read value.", error.toException())
                    }
                })
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to sign user in: ${it.message}")
                Toast.makeText(this, "Incorrect Password ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
