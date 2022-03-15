package com.bill.payment.glpays.Navigation.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Report Fragment"
    }
    val text: LiveData<String> = _text
}