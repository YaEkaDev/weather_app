package com.example.weatherapi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapi.data.Forecastday
import com.example.weatherapi.data.WeatherResponse
import com.example.weatherapi.api.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application): AndroidViewModel(application) {
    val isLoading = MutableLiveData(false)
    val isSetUserInputEnabled = MutableLiveData(false)
    val weatherCurrentData = MutableLiveData<WeatherResponse>()
    val forecastdayData = MutableLiveData<Forecastday>()

    private val compositeDisposable = CompositeDisposable()

    fun loadWeatherData(city: String){

        val disposable = ApiFactory.apiService.getWeatherInfo(city = city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe({
                isLoading.value = true
            })
            .doAfterTerminate({
                isLoading.value = false
            })
            .subscribe({
                weatherCurrentData.value = it
            },
                {

                }
            )
        compositeDisposable.add(disposable)

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}