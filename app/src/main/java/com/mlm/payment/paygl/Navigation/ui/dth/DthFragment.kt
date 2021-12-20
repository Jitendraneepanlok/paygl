package com.mlm.payment.paygl.Navigation.ui.dth

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.mlm.payment.paygl.Navigation.ui.moneyTransfer.MoneyTransferModel
import com.mlm.payment.paygl.databinding.FragmentDthBinding
import com.mlm.payment.paygl.databinding.FragmentMoneyTransferBinding
import com.pay.paygl.Helper.SessionManager

class DthFragment : Fragment() {

    private lateinit var dthModel: DthModel
    private var _binding: FragmentDthBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var user_id: String
    private lateinit var spinopratore: AppCompatSpinner


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dthModel = ViewModelProvider(this).get(DthModel::class.java)
        _binding = FragmentDthBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/

        initView()
        return root
    }

    private fun initView() {
        val Dth_operatore = arrayOf("Select Operator", "Airtel DTH", "Dish DTH", "TataSky DTH", "Videocon DTH")
//        val languages = resources.getStringArray(R.array.dth_operator)
        spinopratore = binding.spinopratore
        if (spinopratore != null) {
            val adapter = activity?.let { ArrayAdapter(it, R.layout.simple_spinner_item, Dth_operatore) }
            spinopratore.adapter = adapter

            spinopratore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // getString(R.string.selected_item) +languages[position],Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

    }
}