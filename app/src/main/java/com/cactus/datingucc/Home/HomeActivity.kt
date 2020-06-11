package com.cactus.datingucc.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.withStyledAttributes
import androidx.viewpager2.widget.ViewPager2
import com.cactus.datingucc.R
import com.cactus.datingucc.User
import com.cianmc.viewpager2.ViewPagerAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val ref: DatabaseReference = Firebase.database.reference
        ref.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                showData(dataSnapshot)
                Log.d("TAG", "$userList")
                val adapter = ViewPagerAdapter(userList)
                viewPager.adapter = adapter
                viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("TAG", "Failed to read value.", error.toException())
            }
        })

    }
    private fun showData(dataSnapshot: DataSnapshot){
        for (p in dataSnapshot.children) {
            //need to add dont put currentUser in
            if (p.child("uid").value == "T5c1pOqwcmWLvVDPEApxrCYex313") {
                val username = p.child("username").value
                val uid = p.child("uid").value
                val register = p.child("registered").value
                val name = p.child("name").value
                val bio = p.child("bio").value
                val age = p.child("age").value
                val course = p.child("course").value
                val location = p.child("location").value
                val profileimguri = p.child("profileimguri").value
                val user = User(
                    uid as String, username as String, register as Boolean,
                    bio as String, age as String, name as String, course as String,
                    location as String, profileimguri as String
                )
                userList.add(user)
            }
        }
    }
}
