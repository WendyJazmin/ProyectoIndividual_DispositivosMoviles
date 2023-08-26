package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()



        binding.btnRegistrar.setOnClickListener {
            if (binding.txtNombre.text.toString().isNotEmpty() && binding.txtContasena.text.toString().isNotEmpty()){
                authWithFirebaseEmail(
                    binding.txtNombre.text.toString(),
                    binding.txtContasena.text.toString()
                )
            }else{
                Toast.makeText(
                    baseContext,
                    "Llene los campos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun authWithFirebaseEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                    Toast.makeText(
//                        baseContext,
//                        "Authentication success.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}