package com.hsofiamunoz.whimfood

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hsofiamunoz.whimfood_ver2.LoginActivity
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.databinding.ActivityRegistroBinding
private const val SPACE= " "
private const val EMPTY = ""


class RegistroActivity : AppCompatActivity() {

    private lateinit var  registroBinding: ActivityRegistroBinding

    //Crear auth para Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher_round)
        supportActionBar!!.title = Html.fromHtml("<font color=\"#1F177D\">" + getString(R.string.app_name) + "</font>")

        super.onCreate(savedInstanceState)
        registroBinding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(registroBinding.root)


        registroBinding.registerButton.setOnClickListener {
            // Variables
            val intent = Intent(this, LoginActivity::class.java)
            val name = registroBinding.fullnameRegisterInputText.text.toString()
            val email_register = registroBinding.emailRegisterInputText.text.toString()
            val password_register = registroBinding.passwordRegisterInputText.text.toString()
            val rep_password_register = registroBinding.repeatPasswordInputText.text.toString()

            val espacios = name.trim()
            val espacios1 = email_register.trim()



            if ( espacios == EMPTY || espacios1 == EMPTY)
            {
                Toast.makeText(this,getString(R.string.spaces3), Toast.LENGTH_SHORT).show()
            }
            else
            {   if (name.isNotEmpty() && email_register.isNotEmpty() && password_register.isNotEmpty() && rep_password_register.isNotEmpty())
                {
                    if (password_register == rep_password_register)
                    {
                        if (password_register.length >= 6)
                        {
                            registroBinding.repeatPasswordTextInputLayout.error = null
                            intent.putExtra("name", name)
                            intent.putExtra("email_register", email_register)
                            intent.putExtra("password_register", password_register)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                        }
                        else
                            Toast.makeText(this, getString(R.string.password_digits), Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_SHORT).show()
                }
                else
                Toast.makeText(this, getString(R.string.missing_parameters), Toast.LENGTH_SHORT).show()


            }


        }



    }


}