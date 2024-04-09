package com.example.androidstudioionhaxproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidstudioionhaxproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signUpBinding.loginBtn.setOnClickListener {
            val username = signUpBinding.userName.text.toString()
            val email = signUpBinding.userEmail.text.toString()
            val password = signUpBinding.password.text.toString()
            val confirmPassword = signUpBinding.ConfirmPassword.text.toString()
            val mobile = signUpBinding.mobile.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = User(username, email, password, mobile)
                            val userId = task.result?.user?.uid // Retrieve the userId
                            if (userId != null) {
                                storeUserData(userId, user) // Pass userId to storeUserData function
                                moveToStudentDetails(
                                    email,
                                    userId
                                ) // Pass both email and userId to moveToStudentDetails
                            } else {
                                Toast.makeText(
                                    this,
                                    "User creation failed: Unable to retrieve user ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }
    }

    private fun storeUserData(userId: String, user: User) {
        val useRef = database.child("Ionhax").child("userDetails")
        useRef.child(userId).setValue(user).addOnSuccessListener {
            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun moveToStudentDetails(email: String, userId: String) {
        val intent = Intent(this, StudentDetailsPage::class.java)
        intent.putExtra("email", email)
        intent.putExtra("userId", userId) // Pass userId to StudentDetailsPage
        startActivity(intent)
    }
}