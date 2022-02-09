package com.mlm.payment.paygl.Navigation.ui.home

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.mlm.payment.paygl.Adapter.DashBoardAdapter
import com.mlm.payment.paygl.Fragments.HomeSliderFragment
import com.mlm.payment.paygl.Helper.IndicatorLayout
import com.mlm.payment.paygl.Model.DashBoardModel
import com.mlm.payment.paygl.Model.PayglXXXXXXXX
import com.mlm.payment.paygl.R
import com.pay.paygl.Adapter.IntroSliderAdapter
import com.pay.paygl.Network.ApiClient
import kotlinx.android.synthetic.main.activity_welcome_slider.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import androidx.navigation.NavController
import androidx.navigation.Navigation.*
import androidx.navigation.findNavController
import com.mlm.payment.paygl.Pojo.DashBoardPageResponse
import com.mlm.payment.paygl.Pojo.Service
import com.mlm.payment.paygl.Pojo.UserDetailsResponse
import com.mlm.payment.paygl.databinding.FragmentHomeBinding
import com.pay.paygl.Activity.LoginActivity
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Model.LogoutModel
import com.pay.paygl.Model.PayglXX
import com.pay.paygl.Model.PayglXXXX
import com.pay.paygl.Model.UserDetailsModel
import com.pay.paygl.Pojo.LogoutResponse
import java.lang.StringBuilder
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homerecycler: RecyclerView
    private lateinit var adapter: DashBoardAdapter
    private lateinit var vpIntroSlider: ViewPager2
    private lateinit var indicatorLayout: IndicatorLayout
    private var dataList = mutableListOf<Service>()
    private val fragmentList = ArrayList<Fragment>()
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 5000 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 7000 // time in milliseconds between successive task executions.

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    private lateinit var txtnews: AppCompatTextView
    private lateinit var animation: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/
        initView()
        getDashBoardData()
        AutoViewPager()
        getUserDetails()
        return root
    }

    override fun onStart() {
        super.onStart()
        navController =
            requireActivity().findNavController(R.id.nav_host_fragment_content_dash_board)
    }

    private fun initView() {
        txtnews = binding.txtnews
        homerecycler = binding.homerecycler
        var LayoutManager = GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
        homerecycler.layoutManager = LayoutManager
    }

    private fun getUserDetails() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getUserDetails(UserDetailsModel(PayglXXXX(user_id)))

        apiInterface.enqueue(object : retrofit2.Callback<UserDetailsResponse> {

            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {

                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    var arraylist = ArrayList(response.body()?.Paygl?.News)
                    for (i in arraylist) {
                        val stringBuilder = StringBuilder()
                        stringBuilder.append(arraylist+ "");
                        txtnews.setText(stringBuilder.append("").toString()+i.txtnews)
                        animation = AnimationUtils.loadAnimation(activity, R.anim.textanimation)
                        txtnews.startAnimation(animation)
                    }
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun AutoViewPager() {
        indicatorLayout = binding.indicatorLayout
        vpIntroSlider = binding.vpIntroSlider


        val adapter = activity?.let { IntroSliderAdapter(it) }
        vpIntroSlider.adapter = adapter
        fragmentList.addAll(
            listOf(
                HomeSliderFragment(),
                HomeSliderFragment(),
                HomeSliderFragment()
            )
        )
        if (adapter != null) {
            adapter.setFragmentList(fragmentList)
        }
        if (adapter != null) {
            indicatorLayout.setIndicatorCount(adapter.itemCount)
        }
        indicatorLayout.selectCurrentPosition(0)
        
        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            val NUM_PAGES = 0;
            if (currentPage === NUM_PAGES - 1) {
                currentPage = 0
            }
            vpIntroSlider.setCurrentItem(currentPage++, true)
        }

        timer = Timer()

        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
        registerListeners()
    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)
                if (position < fragmentList.lastIndex) {
                    Toast.makeText(activity, "last", Toast.LENGTH_SHORT)
                } else {

                }
            }
        })
    }

    private fun getDashBoardData() {

        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getDashboardData(DashBoardModel(PayglXXXXXXXX()))

        apiInterface.enqueue(object : retrofit2.Callback<DashBoardPageResponse> {

            override fun onResponse(call: Call<DashBoardPageResponse>, response: Response<DashBoardPageResponse>) {

                if (response.isSuccessful) {
                    if (response.body()?.Paygl?.resMessage.equals("1")) {
                        Log.e("Response", "" + response.body()?.Paygl?.response)
                        Toast.makeText(
                            activity,
                            response.body()?.Paygl?.response,
                            Toast.LENGTH_SHORT
                        ).show()
                        adapter = activity?.let { DashBoardAdapter(it) }!!
                        homerecycler.adapter = adapter
                        response.body()?.Paygl?.let {
                            dataList.clear()
                            dataList.addAll(it?.Services)
                            adapter.setDataList(dataList)
                            adapter.notifyDataSetChanged()
                        }
                        adapter.setOnItemClickListner(object :
                            DashBoardAdapter.onItemClickedListner {
                            override fun onItemclicked(position: Int) {
                                Log.e("postion", "" + position)
                                when (position) {
                                    0 -> navController.navigate(R.id.action_HomeFragment_to_kycFragment)
                                    1 -> navController.navigate(R.id.action_HomeFragment_to_prepaidFragment)
                                    2 -> navController.navigate(R.id.action_HomeFragment_to_dthFragment)
                                    7 -> navController.navigate(R.id.action_HomeFragment_to_moneyTransferFragment)
                                    12 -> navController.navigate(R.id.action_HomeFragment_to_addFundFragment)
                                    14 -> navController.navigate(R.id.action_HomeFragment_to_galleryFragment)
                                    15 -> CallLogout()
                                }
                            }
                        })

                        pDialog.dismiss()
                    } else {
                        Toast.makeText(
                            activity,
                            response.body()?.Paygl?.response,
                            Toast.LENGTH_SHORT
                        ).show()
                        pDialog.dismiss()
                    }

                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<DashBoardPageResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun CallLogout() {
        val dialog = activity?.let { Dialog(it, R.style.DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.custom_logout_layout)
        dialog?.setCancelable(true)

        val txt_cancel: AppCompatTextView = dialog!!.findViewById(R.id.txt_cancel)
        txt_cancel.setOnClickListener {
            dialog.dismiss()
        }

        val txt_yes: AppCompatTextView = dialog!!.findViewById(R.id.txt_yes)
        txt_yes.setOnClickListener {
            callLogoutApi()
            dialog.dismiss()
        }

        dialog?.window!!.setGravity(Gravity.CENTER)
        dialog?.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

    }

    private fun callLogoutApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.LogOut(LogoutModel(PayglXX(user_id/*"1"*/)))

        apiInterface.enqueue(object : retrofit2.Callback<LogoutResponse> {

            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    Toast.makeText(
                        activity,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    ).show()

                    callnewPage()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(
                        activity,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    ).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun callnewPage() {
        sessionManager.logout()
        startActivity(Intent(activity, LoginActivity::class.java))

    }
}