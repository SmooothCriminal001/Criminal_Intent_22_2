package com.bignerdranch.android.criminal_intent_22_2

import android.app.Application

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}