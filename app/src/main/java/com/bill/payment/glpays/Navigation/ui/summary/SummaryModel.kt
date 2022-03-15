package com.bill.payment.glpays.Navigation.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SummaryModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Summary Fragment"
    }
    val text: LiveData<String> = _text
}