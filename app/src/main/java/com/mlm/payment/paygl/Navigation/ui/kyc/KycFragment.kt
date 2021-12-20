package com.mlm.payment.paygl.Navigation.ui.kyc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mlm.payment.paygl.databinding.FragmentKycBinding
import com.pay.paygl.Helper.SessionManager

class KycFragment : Fragment() {
    private lateinit var kycModel: KycModel
    private var _binding: FragmentKycBinding? = null
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        kycModel = ViewModelProvider(this).get(KycModel::class.java)
        _binding = FragmentKycBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sessionManager = SessionManager(activity)

        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        /*contactUsModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        return root
    }
}