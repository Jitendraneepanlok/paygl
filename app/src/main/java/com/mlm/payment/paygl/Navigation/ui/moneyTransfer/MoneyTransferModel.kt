package com.mlm.payment.paygl.Navigation.ui.moneyTransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoneyTransferModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Money Transfer Fragment"
    }
    val text: LiveData<String> = _text
}