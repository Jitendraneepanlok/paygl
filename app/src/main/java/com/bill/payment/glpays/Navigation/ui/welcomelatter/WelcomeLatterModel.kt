package com.bill.payment.glpays.Navigation.ui.welcomelatter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeLatterModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is welcome Letter Fragment"
    }
    val text: LiveData<String> = _text
}