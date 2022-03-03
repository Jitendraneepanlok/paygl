package com.bill.payment.glpays.Navigation.ui.addFund

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.databinding.FragmentAddFundBinding

class AddFundFragment : Fragment() {

    private lateinit var addFundModel: AddFundModel
    private var _binding: FragmentAddFundBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addFundModel = ViewModelProvider(this).get(AddFundModel::class.java)
        _binding = FragmentAddFundBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}