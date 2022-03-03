package com.bill.payment.glpays.Navigation.ui.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactUsModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add Contact Fragment"
    }
    val text: LiveData<String> = _text
}