package com.example.mychatapp

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        photo_button_register.setOnClickListener {
            performImageSelector()
        }

        registerButton_button_register.setOnClickListener {
            performRegister()
        }

        loginButton_textView_register.setOnClickListener {
            Log.d("RegisterActivity", "go to login page clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performImageSelector(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data?.data
            photoCircle_imageView_register.setImageURI(selectedPhotoUri)
            photo_button_register.alpha =0f

//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
//            val bitmapDrawable = BitmapDrawable(bitmap)
//
//            photo_button_register.setBackgroundDrawable(bitmapDrawable)
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
                            Log.d("RegisterActivity","Success, User uid is: ${task.result?.user?.uid}")

                            performUploadPhotoToFirebaseStorage()
                            Toast.makeText(this,"Registered Success!", Toast.LENGTH_SHORT).show()
                        } else {
                            //if register failed
                            Log.d("RegisterActivity", "Failed")
                            Toast.makeText(this,"Failed to register!", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }
                    }
                    .addOnFailureListener(this){ task ->
                        Log.d("RegisterActivity", "Failed to create user: ${task.message}")
                    }
            }
        }
        else{
            Log.d("RegisterActivity", "Confirm password does not match with password!")
            Toast.makeText(this, "Password does not match!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun performUploadPhotoToFirebaseStorage(){
        if (selectedPhotoUri == null){
            return
        }

        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { task ->
                Log.d("RegisterActivity", "image uploaded to firebase! ${task.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener { task ->
                    Log.d("RegisterActivity", "PhotoLocation: $task")

                    performSaveUserToDatabase(task.toString())
                }
            }
            .addOnFailureListener { task ->
                Log.d("RegisterActivity", "Failed to upload image: $task")
            }
    }

    private fun performSaveUserToDatabase(photoURL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, username_editText_register.text.toString(), photoURL)

        ref.setValue(user)
            .addOnSuccessListener { task ->
                Log.d("RegisterActivity", "Registered successfully to firebase database: $task")
            }
            .addOnFailureListener { task ->
                Log.d("RegisterActivity", "Failed to register to database: $task")
            }
    }
}

class User(val uid: String, val username: String, val profilePhotoURL: String)
