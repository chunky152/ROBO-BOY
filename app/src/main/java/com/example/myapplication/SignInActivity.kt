package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.signIn).setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString().trim()
            val password = findViewById<EditText>(R.id.password).text.toString().trim()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task->
                if (task.isSuccessful) {
                    startActivity(Intent(this, NavigationDrawer::class.java))
                    finish()
                } else {
                    val message = task.exception?.localizedMessage ?: "Login Failed"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    Log.e("AUTH", "Error logging in", task.exception)
                }

            }
        }
    }
}