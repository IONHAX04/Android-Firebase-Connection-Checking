package com.example.androidstudioionhaxproject


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidstudioionhaxproject.databinding.ActivityStudentDetailsPageBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class StudentDetailsPage : AppCompatActivity() {

    private lateinit var studentDetailsPageBinding: ActivityStudentDetailsPageBinding
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        studentDetailsPageBinding = ActivityStudentDetailsPageBinding.inflate(layoutInflater)
        setContentView(studentDetailsPageBinding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val email = intent.getStringExtra("email")
        val userId = intent.getStringExtra("userId")



        studentDetailsPageBinding.buttonSave.setOnClickListener {
            val gender = studentDetailsPageBinding.editTextGender.text.toString()
            val dob = studentDetailsPageBinding.idTVSelectedDate.text.toString()

            if (userId != null) {
                // Check if userId is available
                val userRef = database.child("Ionhax").child("userDetails").child(userId)
                val additionalDetailsMap = HashMap<String, String>()
                additionalDetailsMap["Gender"] = gender
                additionalDetailsMap["DOB"] = dob
                userRef.child("Additional").setValue(additionalDetailsMap)

                Toast.makeText(this, "Additional details saved successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }


        studentDetailsPageBinding.idBtnPickDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    studentDetailsPageBinding.idTVSelectedDate.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        studentDetailsPageBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioPercentage -> {
                    studentDetailsPageBinding.sslcInputLayout.hint =
                        getString(R.string.sslc_percentage_hint)
                }

                R.id.radioCGPA -> {
                    studentDetailsPageBinding.sslcInputLayout.hint =
                        getString(R.string.sslc_cgpa_hint)
                }
            }
        }


//        studentDetailsPageBinding.sslcSpinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val selectedItem = parent?.getItemAtPosition(position).toString()
//                    val hint = if (selectedItem == "Percentage") {
//                        getString(R.string.sslc_percentage_hint)
//                    } else {
//                        getString(R.string.sslc_cgpa_hint)
//                    }
//                    (findViewById<TextInputLayout>(R.id.sslcInputLayout)).hint = hint
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    // Do nothing
//                }
//            }


        studentDetailsPageBinding.hscSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    val hint = if (selectedItem == "Percentage") {
                        getString(R.string.hsc_percentage_hint)
                    } else {
                        getString(R.string.hsc_cgpa_hint)
                    }
                    (findViewById<TextInputLayout>(R.id.hscInputLayout)).hint = hint
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }

            }


    }
}