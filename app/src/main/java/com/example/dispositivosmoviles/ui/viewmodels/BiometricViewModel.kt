package com.example.dispositivosmoviles.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class BiometricViewModel: ViewModel() {

    var isloading = MutableLiveData<Boolean>()



    suspend fun charginData(){
        isloading.postValue(true)
        delay(5000)
        isloading.postValue(false)
    }

}