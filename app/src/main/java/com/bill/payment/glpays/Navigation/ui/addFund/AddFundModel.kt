package com.bill.payment.glpays.Navigation.ui.addFund

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddFundModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add Fund Fragment"
    }
    val text: LiveData<String> = _text
}