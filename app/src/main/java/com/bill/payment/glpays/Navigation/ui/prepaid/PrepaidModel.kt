package com.bill.payment.glpays.Navigation.ui.prepaid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrepaidModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Prepaid Fragment"
    }
    val text: LiveData<String> = _text
}