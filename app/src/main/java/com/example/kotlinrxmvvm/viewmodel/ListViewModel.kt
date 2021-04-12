package com.example.kotlinrxmvvm.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.kotlinrxmvvm.model.DogBreed
import com.example.kotlinrxmvvm.model.DogDatabase
import com.example.kotlinrxmvvm.model.DogsAPIService
import com.example.kotlinrxmvvm.util.NotificationHelper
import com.example.kotlinrxmvvm.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {
    private val dogsService = DogsAPIService()
    private val disposable = CompositeDisposable()
    private val prefHelpers = SharedPreferencesHelper(getApplication())
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L


    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val dogsLoading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefHelpers.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getFromDatabase()
        } else {
            getFromSever()
        }
    }

    fun refreshNow() {
        getFromSever()
    }

    private fun getFromDatabase() {
        dogsLoading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            storeDataLocally(dogs)
            Toast.makeText(getApplication(), "Retrieve from DB", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFromSever() {
        dogsLoading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        storeDataLocally(t)
                        Toast.makeText(getApplication(), "Retrieve from Server", Toast.LENGTH_SHORT)
                            .show()
                        NotificationHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        dogsLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dataRetrieved(dogsLists: List<DogBreed>) {
        dogs.value = dogsLists
        dogsLoadError.value = false
        dogsLoading.value = false
    }

    private fun storeDataLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dataRetrieved(list)
        }
        prefHelpers.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}