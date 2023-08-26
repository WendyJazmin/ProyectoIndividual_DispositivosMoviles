package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding
import com.example.dispositivosmoviles.ui.fragments.FirstFragment
import com.example.dispositivosmoviles.ui.fragments.SecondFragment
import com.example.dispositivosmoviles.ui.fragments.ThirdFragment
import com.example.dispositivosmoviles.ui.utilities.FragmentsManager
import com.google.android.material.snackbar.Snackbar

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("UCE","Entrada a Create")
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()

        var name : String = ""

        binding.txtName.text = "Bienvenido $name"
        Log.d("UCE", "Entrando a Start")

        //super.onStart()
        /* FragmentsManager().replaceFragment(supportFragmentManager,
             binding.frmContainer.id, FirstFragment()
         )*/


        initClass()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun initClass(){
        /*Log.d("uce", "Entrando a start")  debug en la terminal*/
        /*  binding.btnRetorno.setOnClickListener{
              Log.d("UCE", "Entrando al click de retorno")
              var intent= Intent(this, ActivityMainBinding::class.java)
              startActivity(intent)

              /*Snackbar.make(
                  binding.loginSegundo,"regresando",
                  Snackbar.LENGTH_LONG).show()*/
          }
  */
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.inicio -> {
                    FragmentsManager().replaceFragment(supportFragmentManager,
                        binding.frmContainer.id, FirstFragment()
                    )
                    true
                }
                R.id.favoritos -> {
                    FragmentsManager().replaceFragment(supportFragmentManager,
                        binding.frmContainer.id, SecondFragment()
                    )
                    true
                }
                R.id.apis -> {
                    FragmentsManager().replaceFragment(supportFragmentManager,
                        binding.frmContainer.id, ThirdFragment()
                    )
                    true
                }
                R.id.salir -> {
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }

}