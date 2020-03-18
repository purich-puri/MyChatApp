package com.example.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton_button_register.setOnClickListener {
            val email = email_editText_register.text.toString()
            val password = password_editText_register.text.toString()

            Log.d("MainActivity", "Email: $email")
            Log.d("MainActivity", "Password: $password")
        }

        loginButton_textView_register.setOnClickListener {
            Log.d("MainActivity", "go to login page clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
