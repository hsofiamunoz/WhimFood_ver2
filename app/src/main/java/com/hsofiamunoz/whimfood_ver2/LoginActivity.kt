package com.hsofiamunoz.whimfood_ver2

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hsofiamunoz.whimfood.RegistroActivity
import com.hsofiamunoz.whimfood_ver2.databinding.ActivityLoginBinding



class LoginActivity : AppCompatActivity() {

    private lateinit var  loginBinding: ActivityLoginBinding

    //Create auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher_round)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#1F177D\">" + getString(R.string.app_name) + "</font>")

        super.onCreate(savedInstanceState)

        // Variables del intent y Binding
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // FireBase
        auth = Firebase.auth

        val data = intent.extras

        // Botón para cambiar de login a main, iniciar sesión
        loginBinding.loginButton.setOnClickListener{
            // Variables
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)

            val email = loginBinding.emailInputText.text.toString()
            val password = loginBinding.passwordInputText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                signIn()
            }


        }
       /* loginBinding.loginButton.setOnClickListener {

            // Variables
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)

            val email = loginBinding.emailInputText.text.toString()
            val password = loginBinding.passwordInputText.text.toString()

            // En el login si la contraseña y el correo estan vacios se muestra un mensaje
            if (email.isNotEmpty() && password.isNotEmpty()){

                // Registro Primera vez.
                if (data?.getString("email_register") == email ){ // Se compara el correo ingresado con el que se registra
                    if(data.getString("password_register") == password){ // Se compara la contraseña  con la que se registra
                            intent.putExtra("email", email)
                            intent.putExtra("password",password)
                            startActivity(intent)
                            finish()
                    }
                    else
                        Toast.makeText(this,getString(R.string.pas1),Toast.LENGTH_SHORT).show()
                }
                else{
                   // Ingresa despues de cerrar sesion
                    if (data?.getString("email_login") == email){
                        if(data?.getString("password_login")== password){
                            intent.putExtra("email", email)
                            intent.putExtra("password",password)
                            startActivity(intent)
                            finish()
                        }
                        else
                            Toast.makeText(this,getString(R.string.pas1),Toast.LENGTH_SHORT).show()
                    }
                    else
                       Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                }
                }
            else
                Toast.makeText(this,getString(R.string.missing_parameters), Toast.LENGTH_SHORT).show()

        }*/

        loginBinding.registerLink.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn() {
        val email = loginBinding.emailInputText.text.toString()
        val password = loginBinding.passwordInputText.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
                else {
                    var msg = ""
                    if(task.exception?.localizedMessage == "The email address is badly formatted.")
                        msg = "El correo esta mal escrito"
                    else if(task.exception?.localizedMessage == "There is no user record corresponding to this identifier. The user may have been deleted.")
                        msg = "No existe una cuenta con este correo electrónico "
                    else if(task.exception?.localizedMessage == "The password is invalid or the user does not have a password.")
                        msg = "Correo o contraseña inválida"

                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, msg,
                        Toast.LENGTH_SHORT).show()
                }

            }
    }
}