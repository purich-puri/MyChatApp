package com.example.mychatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        loginButton_button_login.setOnClickListener {
            performLogin()
        }

        registerButton_textView_login.setOnClickListener {
            finish()
        }
    }

    private fun performLogin(){
        val email = login_editText_login.text.toString()
        val password = password_editText_login.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                Log.d("LoginActivity", "Logged in as: ${task.result?.user?.uid}")
            }
            .addOnFailureListener(this){ task ->
                Log.d("Login Activity", "Failed to login: ${task.message}")
            }
    }
}