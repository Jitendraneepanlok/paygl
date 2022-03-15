package com.bill.payment.glpays.Navigation.ui.summary

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bill.payment.glpays.Adapter.DashBoardAdapter
import com.bill.payment.glpays.Adapter.SummaryAdapter
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.AppSummaryModel
import com.bill.payment.glpays.Model.DashBoardModel
import com.bill.payment.glpays.Model.PayglXXXXXXXX
import com.bill.payment.glpays.Model.PayglXXXXXXXXXXXX
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.DashBoardPageResponse
import com.bill.payment.glpays.Pojo.IncomeReport
import com.bill.payment.glpays.Pojo.Service
import com.bill.payment.glpays.Pojo.SummaryPojo
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.FragmentHomeBinding
import com.bill.payment.glpays.databinding.FragmentSummaryBinding
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response


class SummaryFragment : Fragment() {
    private lateinit var summaryModel: SummaryModel
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        summaryModel = ViewModelProvider(this).get(SummaryModel::class.java)
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/

        return root
    }



    override fun onStart() {
        super.onStart()
        navController = requireActivity().findNavController(R.id.nav_host_fragment_content_dash_board)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}