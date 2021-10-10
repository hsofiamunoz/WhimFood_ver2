package com.hsofiamunoz.whimfood

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsofiamunoz.whimfood_ver2.LoginActivity
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.data.User
import com.hsofiamunoz.whimfood_ver2.data.UserProfile
import com.hsofiamunoz.whimfood_ver2.databinding.ActivityRegistroBinding
private const val SPACE= " "
private const val EMPTY = ""


class RegistroActivity : AppCompatActivity() {

    private lateinit var  registroBinding: ActivityRegistroBinding

    //Crear auth para Firebase
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher_round)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#1F177D\">" + getString(R.string.app_name) + "</font>")

        super.onCreate(savedInstanceState)
        registroBinding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(registroBinding.root)


        auth = Firebase.auth

        registroBinding.registerButton.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val email = registroBinding.emailRegisterInputText.text.toString()
        val password = registroBinding.passwordRegisterInputText.text.toString()
        val rep_password = registroBinding.repeatPasswordInputText.text.toString()
        val name = registroBinding.fullnameRegisterInputText.text.toString()

        if(password != rep_password)
            Toast.makeText(this,"Las contraseñas deben ser iguales",Toast.LENGTH_SHORT).show()
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Register", "createUserWithEmail:success")
                        Toast.makeText(this, "Registro exitoso",
                            Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        createUSer(email)

                    } else {
                        var msg = ""
                        if(task.exception?.localizedMessage == "The email address is badly formatted.")
                            msg = "El correo esta mal escrito"
                        else if(task.exception?.localizedMessage == "The given password is invalid. [ Password should be at least 6 characters ]")
                            msg = "La contraseña debe tener al menos 6 caracteres"
                        else if(task.exception?.localizedMessage == "The email address is already in use by another account.")
                            msg = "Ya existe una cuenta registrada con este  correo"

                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, msg,
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    private fun createUSer(email: String) {
        var id = auth.currentUser?.uid
        id?.let {id ->
            val name = registroBinding.fullnameRegisterInputText.text.toString()
            //val user = User(id = id, email = email,name = name)
            val user = UserProfile(id = id, email = email,name = name, urlPicture = "https://firebasestorage.googleapis.com/v0/b/whimfood-ver2.appspot.com/o/picture_default.png?alt=media&token=e6430b16-ea0b-424e-bf13-91475d8ea6d5", location = "")

            val db = Firebase.firestore

            db.collection("users").document(id)
                .set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("createInDb", "DocumentSnapshot added with ID: ${id}")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w("createInDb", "Error adding document", e)
                }
        }

    }


}