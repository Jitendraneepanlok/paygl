package com.pay.paygl.Helper

import android.content.Context
import android.preference.PreferenceManager

import android.content.SharedPreferences


class SessionManager(context: Context?) {
    private val sharedPreferences: SharedPreferences
    fun setValue(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun setslider(key: String?, value: Boolean?) {
        sharedPreferences.edit().putBoolean(key, value!!).apply()
    }

    fun getSlider(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getUserData(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun setValueBoolean(key: String?, value: Boolean?) {
        sharedPreferences!!.edit().putBoolean(key, value!!).apply()
    }

    fun getValueBoolean(key: String?): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun logout() {
        //val slider = getValue("slider")
        sharedPreferences.edit().clear().apply()
        //setValue("slider", slider)

    }

    companion object {
        var NAME = "name"
        var EMAIL = "email"
        var ADDRESS = "address"
        var NUMBER = "number"
        var REFERRAL = "referral"
        var VALUE = "value"
        var User_Id = "txtuserID"
        var Login_Id = "txtLoginID"
    }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
}