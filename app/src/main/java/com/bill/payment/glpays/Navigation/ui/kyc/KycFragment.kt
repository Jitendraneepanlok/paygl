package com.bill.payment.glpays.Navigation.ui.kyc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.databinding.FragmentKycBinding

class KycFragment : Fragment() {
    private lateinit var kycModel: KycModel
    private var _binding: FragmentKycBinding? = null
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    private lateinit var dashboardid: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        kycModel = ViewModelProvider(this).get(KycModel::class.java)
        _binding = FragmentKycBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sessionManager = SessionManager(activity)

        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)

        // Here get product ID from Home page Selected product item from list
        dashboardid = getArguments()?.getString("DasgboardItemClicked_ID").toString()
        Log.e("DasgboardItemClicked_ID",""+dashboardid)

        /*contactUsModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        return root
    }
}