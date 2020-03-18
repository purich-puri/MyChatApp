package com.example.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton_button_register.setOnClickListener {
            performRegister()
        }

        loginButton_textView_register.setOnClickListener {
            Log.d("MainActivity", "go to login page clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(){
        val email = email_editText_register.text.toString()
        val password = password_editText_register.text.toString()
        val passwordConfirm = passwordConfirm_editText_register.text.toString()

        if(email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            return
        }
        if (password == passwordConfirm){
            if(password.length < 6){
                //password length too short
                Toast.makeText(this,"Password length too short", Toast.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //register success
                            Log.d("MainActivity","Success, User uid is: ${task.result?.user?.uid}"
                            )

                            Toast.makeText(this,"Registered Success!", Toast.LENGTH_SHORT).show()
                        } else {
                            //if register failed
                            Log.d("MainActivity", "Failed")
                            Toast.makeText(this,"Failed to register!", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }
                    }
                    .addOnFailureListener(this){ task ->
                        Log.d("MainActivity", "Failed to create user: ${task.message}")
                    }
            }
        }
        else{
            Log.d("MainActivity", "Confirm password does not match with password!")
            Toast.makeText(this, "Password does not match!", Toast.LENGTH_SHORT).show()

        }
    }
}
