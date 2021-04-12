package com.example.kotlinrxmvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.kotlinrxmvvm.model.DogBreed
import com.example.kotlinrxmvvm.model.DogDatabase
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : BaseViewModel(application) {
    val currDog = MutableLiveData<DogBreed>()

    fun displaySelectedDogDetail(dogUUID: Int) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao().getDogInfo(dogUUID)
            currDog.value = dao
        }
    }
}