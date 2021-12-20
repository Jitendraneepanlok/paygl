package com.mlm.payment.paygl.Navigation.ui.addFund

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mlm.payment.paygl.Adapter.DashBoardAdapter
import com.mlm.payment.paygl.Helper.IndicatorLayout
import com.mlm.payment.paygl.Navigation.ui.home.HomeViewModel
import com.mlm.payment.paygl.Pojo.Service
import com.mlm.payment.paygl.databinding.FragmentAddFundBinding
import com.mlm.payment.paygl.databinding.FragmentHomeBinding
import com.pay.paygl.Helper.SessionManager
import java.util.*
import kotlin.collections.ArrayList

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