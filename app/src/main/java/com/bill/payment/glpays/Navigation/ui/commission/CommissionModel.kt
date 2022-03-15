package com.bill.payment.glpays.Navigation.ui.commission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommissionModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Commission Fragment"
    }
    val text: LiveData<String> = _text
}