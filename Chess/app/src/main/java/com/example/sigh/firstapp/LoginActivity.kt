package com.example.sigh.firstapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    override fun onStart() {
        super.onStart()
        loadMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
    }

    private fun LoginToFireBase(email: String, password: String) {

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val currentUser = mAuth!!.currentUser

                        Toast.makeText(applicationContext, "Successful Login", Toast.LENGTH_SHORT).show()
                        if (currentUser != null) {
                            myRef.child("Users").child(currentUser.email.toString().split("@")[0]).child("isConnected").setValue(currentUser.uid)
                        }
                        loadMain()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()

                    }

                }
    }

    fun btnLoginEvent(view: View) {

        LoginToFireBase(editTextUserName.text.toString(), editTextPassword.text.toString())
    }

    private fun loadMain() {
        val currentUser = mAuth!!.currentUser

        if (currentUser != null) {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", mAuth!!.currentUser!!.email)
            intent.putExtra("uid", mAuth!!.currentUser!!.uid)
            startActivity(intent)
            finish()
        }


    }
}
