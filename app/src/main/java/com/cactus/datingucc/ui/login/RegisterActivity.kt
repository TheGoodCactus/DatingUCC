package com.cactus.datingucc.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
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

import com.cactus.datingucc.R
import com.cactus.datingucc.RegisterActivity2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) /*sets the content to the activity register*/

        val button = register_button
        button.setOnClickListener {
            performRegister() /*runs the preform function */
        }
        register_tologin.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login Activity" )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        register_photobutton.setOnClickListener {
            Log.d( "Register", "Trying to get photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    var SelectPhotoURI: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { /* we get request code plus image into this function */
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) /* does checks makes sure no error has occured and that an image/data was sent */
            Log.d( " RegisterActivity", "Photo was selected|")
            SelectPhotoURI = data?.data /* Gets uri of photo */
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, SelectPhotoURI) /* gets bitmap of selecxted image */
            val bitmapDrawable = BitmapDrawable(bitmap)
            register_photobutton.setBackgroundDrawable(bitmapDrawable)

    }
    private fun performRegister() {
        val username = register_username.text.toString()  /* gets the data from the textboxes and converts it to string */
        val email = register_email.text.toString()
        val pass = register_password.text.toString()
        val conpass = register_conpass.text.toString()

        if (email.isEmpty() || pass.isEmpty() || conpass.isEmpty()) { /* checks if email or password is empty*/
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show() /* equivlant to pop up in javascript uses a device pop up to say Please enter the textboxes */
            return
        }
        else if (pass != conpass){
            Toast.makeText(this, "Please make sure password and confiremed password are equal", Toast.LENGTH_SHORT).show() /* equivlant to pop up in javascript uses a device pop up to say Please enter the textboxes */
            return
        }
        else if (!email.contains("@umail.ucc.ie",ignoreCase=true)){
            Toast.makeText(this, "Please use a ucc email.", Toast.LENGTH_SHORT).show()
        }

        Log.d("RegisterActivity", "Email is:$email") /* logs what the email inputted it *this doesnt have to be there it just helps to know whats happening */
        Log.d("RegisterActivity", "Password is:$pass")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass) /*Contacts the Firebase Auth system to initilazie making a new user*/
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener /* checks to see if it failed to contact the Firebase if not code keeps going */

                // else if successful
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                UploadImageToFireBase()
            }
            .addOnFailureListener{/* if fails does this*/
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun UploadImageToFireBase() {
        if (SelectPhotoURI == null) return
        Log.d("Register","Ran UploadFunction")
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(SelectPhotoURI!!) /* unwraps URI */
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully Uploaded Image:${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActitiyy", "FIle Location:$it")
                    saveUsertoFirebaseDatabase(it.toString())
                }
                    .addOnFailureListener {
                        Log.d("RegisterActivity", "Failed to find FIle Location")
                    }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to find do this thing")
            }
    }
    private fun saveUsertoFirebaseDatabase(Profileimguri: String){
        Log.d("register", "Ran Save user function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        Log.d("register", "got uid")
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val registered = false
        Log.d("register", "got u334id")
        val user = User(uid, register_username.text.toString(), Profileimguri, registered)
        Log.d("register", "got u3434434334id")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Save user data")
                val intent = Intent(this, RegisterActivity2::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to save users data")
            }

    }
}
class User(val uid: String, val username: String, val Profileimguri: String, val registered: Boolean)




