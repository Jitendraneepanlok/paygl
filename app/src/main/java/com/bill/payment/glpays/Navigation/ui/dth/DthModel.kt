package com.bill.payment.glpays.Navigation.ui.dth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DthModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Dth Fragment"
    }
    val text: LiveData<String> = _text
}