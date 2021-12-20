package com.mlm.payment.paygl.Navigation.ui.contactus

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.mlm.payment.paygl.Navigation.ui.addBank.AddBankModel
import com.mlm.payment.paygl.R
import com.mlm.payment.paygl.databinding.FragmentAddBankBinding
import com.mlm.payment.paygl.databinding.FragmentContactUsBinding
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Model.PayglXXXXXX
import com.pay.paygl.Network.ApiClient
import com.pay.paygl.Pojo.AddBankDetailsResponse
import retrofit2.Call
import retrofit2.Response

class ContactusFragment : Fragment() {
    private lateinit var contactUsModel: ContactUsModel
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        contactUsModel = ViewModelProvider(this).get(ContactUsModel::class.java)

        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sessionManager = SessionManager(activity)

        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)

        /*contactUsModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}