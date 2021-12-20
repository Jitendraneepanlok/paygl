package com.mlm.payment.paygl.Navigation.ui.kyc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KycModel  : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Kyc Fragment"
    }
    val text: LiveData<String> = _text
}