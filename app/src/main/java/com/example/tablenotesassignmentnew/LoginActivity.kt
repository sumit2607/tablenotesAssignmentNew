package com.example.tablenotesassignmentnew

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tablenotesassignmentnew.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        auth.currentUser.also {
            if (it != null) {
                user = it
            }
        }
        binding.apply {
            mlinks.setOnClickListener {
                logInUser()
            }

            btnSignupNew.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
               // intent.putExtra("key", value)
                startActivity(intent)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun logInUser() {

        binding.apply {
            if (emailLable.editText?.text.toString().isEmpty()) {
                emailLable.error = "Please enter your Email id"
                emailLable.requestFocus()
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailLable.editText?.text.toString())
                    .matches()
            ) {
                emailLable.error = "Please enter valid Email"
                emailLable.requestFocus()
                return
            }

            if (passwordLable.editText?.text.toString().isEmpty()) {
                passwordLable.error = "Please enter your Password"
                passwordLable.requestFocus()
                return
            }

            if (passwordLable.editText?.text.toString().length < 6) {
                passwordLable.error = "Password is too short\nMust be length between 6-12."
                passwordLable.requestFocus()
                return
            }

            if (passwordLable.editText?.text.toString().length > 12) {
                passwordLable.error = "Password is much long\nMust be length between 6-12."
                passwordLable.requestFocus()
                return
            }

            if (notEmpty()) {

                auth.signInWithEmailAndPassword(
                    emailLable.editText?.text.toString(),
                    passwordLable.editText?.text.toString()
                ).addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        user = auth.currentUser!!

                        Toast.makeText(
                            this@LoginActivity,
                            "Login Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun notEmpty(): Boolean {
        return (binding.emailLable.editText?.text.toString().isNotEmpty()
                && binding.emailLable.editText?.text.toString().isNotEmpty())
    }

}