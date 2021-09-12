package com.hsofiamunoz.whimfood_ver2

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hsofiamunoz.whimfood.RegistroActivity
import com.hsofiamunoz.whimfood_ver2.databinding.ActivityLoginBinding



class LoginActivity : AppCompatActivity() {

    private lateinit var  loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher_round)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#1F177D\">" + getString(R.string.app_name) + "</font>")

        super.onCreate(savedInstanceState)

        // Variables del intent y Binding
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        val data = intent.extras

        // Botón para cambiar de login a main, iniciar sesión
        loginBinding.loginButton.setOnClickListener {

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

        }

        loginBinding.registerLink.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

    }
}