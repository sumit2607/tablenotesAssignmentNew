package com.example.tablenotesassignmentnew

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tablenotesassignmentnew.databinding.ActivitySignUpBinding
import com.example.tablenotesassignmentnew.utlis.Name
import com.example.tablenotesassignmentnew.utlis.adapter.CardData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val PICK_IMAGE_REQUEST = 1
    val REQUEST_PICK = 9162

    var imageUriMain: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        // Initialize Firestore
        val firestore = FirebaseFirestore.getInstance()


        auth.currentUser.also {
            if (it != null) {
                user = it
            }
        }
        binding.apply {

            btnSignup.setOnClickListener {
                uploadUserDataToFireBase()
                signUpUser()
            }
            userImage.setOnClickListener {
               uploadImageToFireBase()
            }
        }

    }

    private fun uploadImageToFireBase() {


// Create an intent to pick an image from the internal storage
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST) // Replace "image.jpg" with the actual filename


    }


    private fun uploadUserDataToFireBase(
    ) {

        val userNameData = binding.nameText.text.toString()
        //  val userAge = binding.etEmailIdSignup.editText.toString()
        val userEmail = binding.emailId.text.toString()
        Log.d(TAG, "uploadUserDataToFireBase: " + userNameData)

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("cards")

        val note = CardData(imageUriMain, userNameData, 0, userEmail)

        collectionRef.add(note)
            .addOnSuccessListener { documentReference ->
                Log.d("UploadData", "Document uploaded with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("UploadData", "Error uploading document", e)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            var imageUri: Uri? = data?.data

            // Use the selected image URI
            if (imageUri != null) {
                imageUriMain = data?.data.toString()
                uploadImage(imageUri)
                // Perform the desired operation with the image URI
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/image.jpg")  // Replace with your desired path and filename

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                // You can retrieve the download URL of the uploaded image from taskSnapshot.metadata?.reference?.downloadUrl()
                val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                // Save the download URL to Firestore or perform any other desired operation
            }
            .addOnFailureListener { e ->
                // Handle the failure case
            }

    }

}


