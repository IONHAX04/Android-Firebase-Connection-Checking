package com.example.androidstudioionhaxproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidstudioionhaxproject.databinding.ActivitySignoutCheckingBinding
import com.google.firebase.auth.FirebaseAuth

class SignoutChecking : AppCompatActivity() {

    private lateinit var signoutChecking: ActivitySignoutCheckingBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signoutChecking = ActivitySignoutCheckingBinding.inflate(layoutInflater)
        setContentView(signoutChecking.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        user = FirebaseAuth.getInstance()
        if (user.currentUser != null){
            user.currentUser?.let{
                signoutChecking.userEmail.text = it.email
            }
        }

        signoutChecking.signout.setOnClickListener{
            user.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}