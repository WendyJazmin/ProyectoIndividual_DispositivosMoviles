package com.example.dispositivosmoviles.ui.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.Location

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker.PermissionResult
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


import androidx.lifecycle.lifecycleScope

import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityLoginBinding
import com.example.dispositivosmoviles.logic.validator.LoginValidator
import com.example.dispositivosmoviles.ui.utilities.MyLocationManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    //Ubicacion y GPS
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback
    private lateinit var client : SettingsClient
    private lateinit var locationSettingRequest : LocationSettingsRequest

    private lateinit var auth: FirebaseAuth




    private var currentLocation: Location? = null

    private val speechToText =
        registerForActivityResult(StartActivityForResult()) { activityResult ->
            val sn = Snackbar.make(
                binding.textView, "",
                Snackbar.LENGTH_LONG
            )
            var message = ""
            when (activityResult.resultCode) {
                RESULT_OK -> {
                    val msg = activityResult
                        .data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                        .toString()

                    if (msg.isNotEmpty()) {
                        val intent = Intent(
                            Intent.ACTION_WEB_SEARCH
                        )
                        intent.setClassName(
                            "com.google.android.googlequicksearchbox",
                            "com.google.android.googlequicksearchbox.SearchActivity"
                        )
                        Log.d("UCE", msg)
                        intent.putExtra(SearchManager.QUERY, msg.toString())
                        startActivity(intent)
                    }
                }

                RESULT_CANCELED -> {
                    message = "Proceso cancelado"
                    sn.setBackgroundTint(resources.getColor(R.color.rojo))
                }

                else -> {
                    message = "Ocurrio un error"
                    sn.setBackgroundTint(resources.getColor(R.color.rojo))
                }
            }

            sn.setText(message)
            sn.show()
        }

    @SuppressLint("MissingPermission")
    private val locationContract = registerForActivityResult(RequestPermission()) { isGranted ->
        when (isGranted == true) {
            true -> {

                client.checkLocationSettings(locationSettingRequest).apply {
                    addOnSuccessListener {
                        val task = fusedLocationProviderClient.lastLocation
                        task.addOnSuccessListener { location ->
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallBack,
                                Looper.getMainLooper()
                            )
                        }
                    }
                    addOnFailureListener{ex ->
                        if(ex is ResolvableApiException){
                            //startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                            ex.startResolutionForResult(
                                this@LoginActivity,
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                            )
                        }

                    }
                }


            }

            shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Snackbar.make(
                    binding.textView,
                    "Ayude con el permiso porfa",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            false -> {
            }

            else -> {
                Snackbar.make(binding.textView, "Permiso Denegado", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        )
            .setMaxUpdates(3)
            .build()

        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (locationResult != null) {
                    locationResult.locations.forEach { location ->
                        currentLocation = location
                        Log.d(
                            "UCE", "Ubicacion: ${location.latitude}," +
                                    "${location.longitude}"
                        )
                    }
                } else {
                    Log.d("UCE", "GPS apagado")
                }
            }
        }
        client = LocationServices.getSettingsClient(this)

        locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()

        auth = Firebase.auth


    }


    private fun signInWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, BiometricActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    private fun recoveryPasswordWithEmail(email: String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        this,
                        "Correo de recuperacio enviado correctamente",
                        Toast.LENGTH_SHORT,
                    ).show()

                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Alert")
                        setMessage("Correo de recuperacion enviado correctamente")
                        setCancelable(true)
                    }
                }

            }
    }

    override fun onStart() {
        super.onStart()

        binding.btnIngresar.setOnClickListener {

            if (binding.txtNombre.text.toString().isNotEmpty() && binding.txtContasena.text.toString().isNotEmpty()){
                signInWithEmailAndPassword(
                    binding.txtNombre.text.toString(),
                    binding.txtContasena.text.toString()
                )
            }else{
                Toast.makeText(
                    baseContext,
                    "Llene los campos.",
                    Toast.LENGTH_SHORT,
                ).show()
            }


        }

        binding.txtvwOlvidoPassword.setOnClickListener {
            if(binding.txtNombre.text.toString().isNotEmpty()){
                recoveryPasswordWithEmail(
                    binding.txtNombre.text.toString()
                )
            }else{
                Toast.makeText(
                    baseContext,
                    "Ingrese su correo electronico en el campo",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        binding.txtvwRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }



        binding.btnTwitter.setOnClickListener {

            locationContract.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

        }

        val appResultLocal = registerForActivityResult(StartActivityForResult()) { resultActivity ->

            val sn = Snackbar.make(
                binding.textView, "",
                Snackbar.LENGTH_LONG
            )

            //contrato
            var message = when (resultActivity.resultCode) {
                RESULT_OK -> {
                    sn.setBackgroundTint(resources.getColor(R.color.blue))
                    resultActivity.data?.getStringExtra("result")
                        .orEmpty()
                }

                RESULT_CANCELED -> {
                    sn.setBackgroundTint(resources.getColor(R.color.rojo))
                    resultActivity.data?.getStringExtra("result")
                        .orEmpty()
                }

                else -> {
                    "Dudoso"
                }
            }

            sn.setText(message)
            sn.show()

        }



        binding.btnResult.setOnClickListener {

            val intentSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM

            )
            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Di algo....."
            )
            speechToText.launch(intentSpeech)

        }

    }

    private suspend fun saveDataStore(stringData: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("usuario")] = stringData
            prefs[stringPreferencesKey("session")] = UUID.randomUUID().toString()
            prefs[stringPreferencesKey("email")] = "dimoviles@uce.edu.ec"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }

    private fun test(){
        var location = MyLocationManager(this)
        location.getUserLocation()
    }
}