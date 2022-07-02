package com.bignerdranch.android.criminal_intent_22_2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    private val _crimes = MutableStateFlow<List<Crime>>(emptyList())

    val crimes : StateFlow<List<Crime>>
    	get() = _crimes.asStateFlow()


    init {
        viewModelScope.launch{
            crimeRepository.getCrimes().collect{
                _crimes.value = it
            }
        }
    }
}