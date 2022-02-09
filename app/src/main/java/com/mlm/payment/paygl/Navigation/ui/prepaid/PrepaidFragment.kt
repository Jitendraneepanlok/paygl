package com.mlm.payment.paygl.Navigation.ui.prepaid

import android.app.ProgressDialog
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.mlm.payment.paygl.Adapter.PlanAdapter
import com.mlm.payment.paygl.Model.*
import com.mlm.payment.paygl.Model.PayglXXXXXXXXX
import com.mlm.payment.paygl.Model.PayglXXXXXXXXXX
import com.mlm.payment.paygl.Model.PayglXXXXXXXXXXX
import com.mlm.payment.paygl.Pojo.*
import com.mlm.payment.paygl.databinding.FragmentPrepaidRechargeBinding
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Network.ApiClient
import retrofit2.Call
import retrofit2.Response

class PrepaidFragment : Fragment() {
    private lateinit var prepaidModel: PrepaidModel
    private var _binding: FragmentPrepaidRechargeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var user_id: String
    private lateinit var spinopratore: AppCompatSpinner
    private  var operatorlist: ArrayList<Operator>? =null
    private var planname: ArrayList<ViewPlan>? = null
    private lateinit var dashboardid: String
    private lateinit var etnumber: AppCompatEditText
    private lateinit var selectedoperator: String
    private lateinit var etplan: AppCompatEditText
    private lateinit var btnpay: AppCompatButton
    private lateinit var etamount :AppCompatEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepaidModel = ViewModelProvider(this).get(PrepaidModel::class.java)
        _binding = FragmentPrepaidRechargeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        // Here get product ID from Home page Selected product item from list
        dashboardid = sessionManager.getUserData(SessionManager.txtid).toString()
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/
        initView()
        getOperator()
        return root
    }

    private fun getOperator() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getOperatorData(OperatorModel(PayglXXXXXXXXX(/*dashboardid*/"2")))
        apiInterface.enqueue(object : retrofit2.Callback<OperatorResponse> {
            override fun onResponse(call: Call<OperatorResponse>, response: Response<OperatorResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    operatorlist = ArrayList()
                    if (operatorlist!=null){
                        response.body()?.Paygl?.let { operatorlist!!.addAll(it?.operator) }
                        val customDropDownAdapter = PlanAdapter(activity, operatorlist!!)
                        spinopratore.adapter = customDropDownAdapter
                        customDropDownAdapter.notifyDataSetChanged()
                    }

                    pDialog.dismiss()

                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<OperatorResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun initView() {
        etamount = binding.etamount
        etplan = binding.etplan
        spinopratore = binding.spinopratore
        if (spinopratore != null) {
            spinopratore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    var rrr: Operator = spinopratore.selectedItem as Operator
                    selectedoperator = rrr.txtopid
                    CallPlanApi(selectedoperator)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }


            }

            etnumber = binding.etnumber
            btnpay = binding.btnpay
            btnpay.setOnClickListener {
                checkValidation()
            }

        }
    }



    private fun CallPlanApi(selectedoperator: String) {
        Log.e("planId", "" + selectedoperator)
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface =
            ApiClient.getClient.getplanData(PlanModel(PayglXXXXXXXXXX(selectedoperator)))
        apiInterface.enqueue(object : retrofit2.Callback<PlanResponse> {
            override fun onResponse(call: Call<PlanResponse>, response: Response<PlanResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    planname = ArrayList()
                    response.body()?.Paygl?.let { planname!!.addAll(it?.ViewPlan) }

                    for (i in 0 until planname!!.size) {
                        etplan.setText(
                            response.body()?.Paygl?.ViewPlan?.get(i)?.txtplanamt + response.body()?.Paygl?.ViewPlan?.get(
                                i
                            )?.txtplandesc
                        )

                    }


                    pDialog.dismiss()

                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<PlanResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun checkValidation() {

        if (etnumber.text?.trim()?.isEmpty()!!){
            etnumber.requestFocus()
            etnumber.setError("Please Enter Mobile no.")
        }else if (etamount.text?.trim()?.isEmpty()!!){
            etamount.requestFocus()
            etamount.setError("Please Enter Recharge Ammount")
        }else{
            CallPaymentApi()
        }
    }

    private fun CallPaymentApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();


        val apiInterface = ApiClient.getClient.getRecharge(AppRechargeModel(PayglXXXXXXXXXXX(
            etamount.text.toString(),
            dashboardid,etnumber.text.toString(),
            selectedoperator,
            user_id)))
        apiInterface.enqueue(object : retrofit2.Callback<AppRechargePojo> {
            override fun onResponse(call: Call<AppRechargePojo>, response: Response<AppRechargePojo>) {
                if (response.isSuccessful) {
                    Log.e("RechargeResponse", "" + response.body()?.Paygl?.response)

                    pDialog.dismiss()

                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AppRechargePojo>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }
}
