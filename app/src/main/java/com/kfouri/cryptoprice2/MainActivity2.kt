package com.kfouri.cryptoprice2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Log.d("Kafu", "Hola Activity 2")
        Log.d("Kafu", "Cantidad: " + AvailableCurrencies.getSize())
    }
}