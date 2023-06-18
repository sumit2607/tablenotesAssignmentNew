package com.example.tablenotesassignmentnew

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tablenotesassignmentnew.databinding.ActivitySignUpBinding
import com.example.tablenotesassignmentnew.utlis.Name
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        auth.currentUser.also {
            if (it != null) {
                user = it
            }
        }
        binding.apply {
            btnSignup.setOnClickListener {
                signUpUser()
            }
        }

    }

    private fun signUpUser() {

        binding.apply {

            if (etNameSignup.editText?.text.toString().isEmpty()) {
                etNameSignup.error = "Please enter your Name"
                etNameSignup.requestFocus()
                return
            }

            if (etEmailIdSignup.editText?.text.toString().isEmpty()) {
                etEmailIdSignup.error = "Please enter your Email id"
                etEmailIdSignup.requestFocus()
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(etEmailIdSignup.editText?.text.toString())
                    .matches()
            ) {
                etEmailIdSignup.error = "Please enter Valid Email"
                etEmailIdSignup.requestFocus()
                return
            }

            if (etPasswordSignup.editText?.text.toString().isEmpty()) {
                etPasswordSignup.error = "Please enter your Password"
                etPasswordSignup.requestFocus()
                return
            }

            if (etPasswordSignup.editText?.text.toString().length < 6) {
                etPasswordSignup.error = "Password is too short\nMust be length between 6-12."
                etPasswordSignup.requestFocus()
                return
            }

            if (etPasswordSignup.editText?.text.toString().length > 12) {
                etPasswordSignup.error = "Password is much long\nMust be length between 6-12."
                etPasswordSignup.requestFocus()
                return
            }

            if (etRePasswordSignup.editText?.text.toString().isEmpty()) {
                etRePasswordSignup.error = "Please re-enter your Password"
                etRePasswordSignup.requestFocus()
                return
            }

            if (etPasswordSignup.editText?.text.toString() != etRePasswordSignup.editText?.text.toString()) {
                etRePasswordSignup.error = "Password does not Match."
                etRePasswordSignup.requestFocus()
                return
            }

            if (identicalPassword() && notEmpty()) {

                auth.createUserWithEmailAndPassword(
                    etEmailIdSignup.editText?.text.toString(),
                    etPasswordSignup.editText?.text.toString()
                )
                    .addOnCompleteListener(this@SignUpActivity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            user = auth.currentUser!!

                            Toast.makeText(
                                this@SignUpActivity,
                                "SignUp Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            sendEmailVerification()

                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            intent.putExtra(Name, etNameSignup.editText?.text.toString())
                            startActivity(intent)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

        }
    }

    private fun sendEmailVerification() {
        user.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email sent to ${binding.etEmailIdSignup.editText?.text.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun identicalPassword(): Boolean {
        return binding.etPasswordSignup.editText?.text.toString() == binding.etRePasswordSignup.editText?.text.toString()
    }

    private fun notEmpty(): Boolean {
        return binding.etNameSignup.editText?.text.toString().isNotEmpty() &&
                binding.etEmailIdSignup.editText?.text.toString().isNotEmpty() &&
                binding.etPasswordSignup.editText?.text.toString().isNotEmpty() &&
                binding.etRePasswordSignup.editText?.text.toString().isNotEmpty()
    }

}