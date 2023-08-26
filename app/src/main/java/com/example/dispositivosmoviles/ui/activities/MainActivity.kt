package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.logic.validator.LoginValidator
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    //se enlaza el binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    //Por buenas practicas de programcion poner las clases en onStart
    override fun onStart() {
        super.onStart()
        initClass()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initClass() {
        //var boton2 = binding.botonUno
        //No es necesario crear variables al usar biding
        binding.btnLogin.setOnClickListener {

            val check = LoginValidator().checkLogin(binding.txtName.text.toString(),binding.txtPass.text.toString())

            if(check){

                var intent = Intent(
                    this,
                    PrincipalActivity::class.java
                )

                intent.putExtra("var1", binding.txtName.text.toString())

                startActivity(intent)

            }else{
                Snackbar.make(binding.textView1,"Usuario o contraseña invalidos", Snackbar.LENGTH_LONG).show()
            }

        }

    }

}