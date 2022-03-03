package com.bill.payment.glpays.Navigation.ui.moneyTransfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.databinding.FragmentMoneyTransferBinding

class MoneyTransferFragment : Fragment() {

    private lateinit var moneyTransferModel: MoneyTransferModel
    private var _binding: FragmentMoneyTransferBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moneyTransferModel = ViewModelProvider(this).get(MoneyTransferModel::class.java)
        _binding = FragmentMoneyTransferBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/


        return root
    }
}