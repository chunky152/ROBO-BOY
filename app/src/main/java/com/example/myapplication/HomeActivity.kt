package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signInLink = findViewById<TextView>(R.id.signInLink)
        signInLink.setOnClickListener({
            startActivity(Intent(this, SignInActivity::class.java))
        })

        val username = findViewById<EditText>(R.id.username).text.toString().trim()
        val email = findViewById<EditText>(R.id.email).text.toString().trim()
        val password = findViewById<EditText>(R.id.password).text.toString().trim()
        val submit = findViewById<TextView>(R.id.button)


        submit.setOnClickListener {
            registerUser(username, email, password)
            startActivity(Intent(this, NavigationDrawer::class.java))
        }
    }

        private fun registerUser(username: String, email: String, password: String){
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    val userMap = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password
                    )
                    db.collection("users").document(user!!.uid).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val message = task.exception?.localizedMessage ?: "Registration Failed"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    Log.e("AUTH", "Error registering user", task.exception)
                }

                }
            }
        }