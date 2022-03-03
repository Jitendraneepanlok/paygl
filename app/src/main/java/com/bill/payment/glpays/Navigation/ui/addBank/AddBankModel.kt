package com.bill.payment.glpays.Navigation.ui.addBank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddBankModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add Bank Fragment"
    }
    val text: LiveData<String> = _text
}