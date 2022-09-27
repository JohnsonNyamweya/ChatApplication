package com.nyamweyajohnson.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nyamweyajohnson.chatapplication.model.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var edtUserName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var mAuth: FirebaseAuth

    private lateinit var userDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtUserName = findViewById(R.id.edt_sign_up_user_name)
        edtEmail = findViewById(R.id.edt_sign_up_email)
        edtPassword = findViewById(R.id.edt_sign_up_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val name = edtUserName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signUp(name, email, password)
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                   Toast.makeText(this@SignUpActivity, "Signed Up Successfully", Toast.LENGTH_SHORT).show()

                    //add user to the database
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this@SignUpActivity, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        userDatabaseReference = FirebaseDatabase.getInstance().reference

        userDatabaseReference.child("User").child(uid).setValue(User(name, email, uid))
    }
}