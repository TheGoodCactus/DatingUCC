package com.cactus.datingucc

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cactus.datingucc.ui.login.LoginActivity
import com.cactus.datingucc.ui.login.NewInfo
import com.cactus.datingucc.ui.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.register_photobutton
import kotlinx.android.synthetic.main.activity_register2.*
import java.util.*


class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        register_photobutton.setOnClickListener {
            Log.d("Register", "Trying to get photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        finish_button.setOnClickListener{
            UploadImageToFireBase()
        }

    }

    var SelectPhotoURI: Uri? = null
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) { /* we get request code plus image into this function */
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) /* does checks makes sure no error has occured and that an image/data was sent */
            Log.d(" RegisterActivity", "Photo was selected|")
        SelectPhotoURI = data?.data /* Gets uri of photo */
        val bitmap = MediaStore.Images.Media.getBitmap(
            contentResolver,
            SelectPhotoURI
        ) /* gets bitmap of selecxted image */
        val bitmapDrawable = BitmapDrawable(bitmap)
        register_photobutton.setBackgroundDrawable(bitmapDrawable)
    }

    private fun UploadImageToFireBase() {
        if (SelectPhotoURI == null) return
        Log.d("Register", "Ran UploadFunction")
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(SelectPhotoURI!!) /* unwraps URI */
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully Uploaded Image:${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActitiyy", "FIle Location:$it")
                    getData(it.toString())
                }
                    .addOnFailureListener {
                        Log.d("RegisterActivity", "Failed to find FIle Location")
                    }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to find do this thing")
            }
    }

    private fun appendToUserData(user: NewInfo) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Save user data")
                //SavedPreferences logged in = true
                val editor = getSharedPreferences("LoggedIn", 0).edit()
                editor.putBoolean("LoggedIn", true)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to save users data")
            }
    }
    private fun getData(profileuri: String){
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users").child("$currentFirebaseUser")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                showData(dataSnapshot,profileuri)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("TAG", "Failed to read value.", error.toException())
            }
    })
    }
    private fun showData(dataSnapshot: DataSnapshot,profileuri: String){
            val username = dataSnapshot.child("username").value
            val uid = dataSnapshot.child("uid").value
            val register = dataSnapshot.child("registered").value
            val name = register_name2.text.toString()  /* gets the data from the textboxes and converts it to string */
            val bio = register_bio.text.toString()
            val age = register_age.text.toString()
            val course = register_course.text.toString()
            val location = register_location.text.toString()
            Log.d("Register","$username")
            val user = NewInfo(uid as String, username as String, register as Boolean, bio, age, name, course, location, profileuri)
            appendToUserData(user)

        }
    }



