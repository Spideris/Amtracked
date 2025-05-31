package com.example.amtraker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amtraker.api.ApiFunctions
import com.example.amtraker.api.Station
import com.example.amtraker.api.StationMeta
import com.example.amtraker.api.Train
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    val thisApi = ApiFunctions()

    private val _trains = MutableLiveData<Map<String, List<Train>>>()
    val trains: LiveData<Map<String, List<Train>>> = _trains

    private val _stations = MutableLiveData<Map<String, StationMeta>>()
    val stations:  LiveData<Map<String, StationMeta>> = _stations

    init {
        reInit()
    }

    fun reInit() {
        fetchTrains()
        fetchStations()
    }

    private fun fetchTrains() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("Train API call beginning")
                val fetchedTrainData = thisApi.TrainApiCall()
                println("Train API call finished")
                _trains.postValue(fetchedTrainData)
            } catch (e: Exception) {
                Log.e("Train API Call", "Error during Train API call: ${e.message}")
            }
        }
    }

    private fun fetchStations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("Station API call beginning")
                val fetchedStationData = thisApi.StationApiCall()
                println("Station API call finished")
                _stations.postValue(fetchedStationData)
            } catch (e: Exception) {
                Log.e("Station API Call", "Error during Station API call: ${e.message}")
            }
        }
    }

    fun getTrainById(trainId: String): Train? {
        return _trains.value?.values?.flatten()?.find { it.trainID == trainId }
    }

    fun getTrainsByKeys(trainKeys:List <String>): List<Train>? {

        val foundTrains = mutableListOf<Train>()

        for (key in trainKeys) {
            getTrainById(key)?.let { foundTrains.add(it) }
        }
        return foundTrains
    }

    fun getStationByCode(code: String) : StationMeta? {
        return _stations.value?.values?.find { it.code == code }
    }
}