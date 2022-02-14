package com.mlm.payment.paygl.Navigation.ui.dth

import android.R
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.mlm.payment.paygl.Adapter.PlanAdapter
import com.mlm.payment.paygl.Model.*
import com.mlm.payment.paygl.Model.PayglXXXXXXXXX
import com.mlm.payment.paygl.Model.PayglXXXXXXXXXX
import com.mlm.payment.paygl.Model.PayglXXXXXXXXXXX
import com.mlm.payment.paygl.Pojo.*
import com.mlm.payment.paygl.databinding.FragmentDthBinding
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Network.ApiClient
import retrofit2.Call
import retrofit2.Response

class DthFragment : Fragment() {

    private lateinit var dthModel: DthModel
    private var _binding: FragmentDthBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var user_id: String
    private lateinit var spinopratore: AppCompatSpinner
    private lateinit var dashboardid: String
    private lateinit var root: View
    private lateinit var selectedoperator: String
    private  var operatorlist: ArrayList<Operator>? =null
    private var planname: ArrayList<ViewPlan>? = null
    private lateinit var etplan: AppCompatEditText
    private lateinit var btnpay: AppCompatButton
    private lateinit var etamount :AppCompatEditText
    private lateinit var etnumber: AppCompatEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dthModel = ViewModelProvider(this).get(DthModel::class.java)
        _binding = FragmentDthBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        // this id for when login than getting into login response api
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()

        // Here get product ID from Home page Selected product item from list
        dashboardid = getArguments()?.getString("DasgboardItemClicked_ID").toString()
        Log.e("DasgboardItemClicked_ID",""+dashboardid)
        root = binding.root

        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/
        getOperator()

        initView()
        return root
    }
    private fun getOperator() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getOperatorData(OperatorModel(PayglXXXXXXXXX(dashboardid/*"2"*/)))
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
            etamount = binding.etamount
            etplan = binding.etplan
            etnumber = binding.etnumber
            btnpay = binding.btnpay
            btnpay.setOnClickListener {
                checkValidation()
            }
        }
    }
    private fun CallPlanApi(selectedoperator: String) {
        Log.e("selectedId", "" + selectedoperator)
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getplanData(PlanModel(PayglXXXXXXXXXX(/*"9"*/selectedoperator)))
        apiInterface.enqueue(object : retrofit2.Callback<PlanResponse> {
            override fun onResponse(call: Call<PlanResponse>, response: Response<PlanResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
                    planname = ArrayList()
                    if (planname!=null){
                        response.body()?.Paygl?.let { planname!!.addAll(it?.ViewPlan) }
                        for (i in 0 until planname!!.size) {
                            etplan.setText(response.body()?.Paygl?.ViewPlan?.get(i)?.txtplanamt +"\n"+ response.body()?.Paygl?.ViewPlan?.get(i)?.txtplandesc)
                        }
                        pDialog.dismiss()
                    }


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
            CallRechargeApi()
        }
    }

    private fun CallRechargeApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.mlm.payment.paygl.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();


        val apiInterface = ApiClient.getClient.getRecharge(
            AppRechargeModel(PayglXXXXXXXXXXX(etamount.text.toString(), dashboardid,etnumber.text.toString(), selectedoperator, user_id)))

        apiInterface.enqueue(object : retrofit2.Callback<AppRechargePojo> {
            override fun onResponse(call: Call<AppRechargePojo>, response: Response<AppRechargePojo>) {
                if (response.isSuccessful) {
                    Log.e("RechargeResponse", "" + response.body()?.Paygl?.response)

                    if (response.body()?.Paygl?.recharge_status!=null) {
                        callStatusDialog(response.body()?.Paygl?.recharge_status)
                    }
                    pDialog.dismiss()

                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
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

    private fun callStatusDialog(rechargeStatus: String?) {
        val dialog = activity?.let { Dialog(it, com.mlm.payment.paygl.R.style.DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.mlm.payment.paygl.R.layout.status_layout)
        dialog?.setCancelable(true)

        var txt_recharge_staus : AppCompatTextView
        var txt_thanks : AppCompatTextView
        var txt_sucess : AppCompatTextView
        var rl1 : RelativeLayout
        var img_status : AppCompatImageView

        txt_recharge_staus = dialog!!.findViewById(com.mlm.payment.paygl.R.id.txt_recharge_staus)
        txt_thanks = dialog!!.findViewById(com.mlm.payment.paygl.R.id.txt_thanks)
        txt_sucess = dialog!!.findViewById(com.mlm.payment.paygl.R.id.txt_sucess)
        rl1 = dialog!!.findViewById(com.mlm.payment.paygl.R.id.rl1)
        img_status = dialog!!.findViewById(com.mlm.payment.paygl.R.id.img_status)

        if (rechargeStatus.equals("Success")){
            txt_sucess.setText(rechargeStatus+"!")
            txt_sucess.setTextColor(resources.getColor(com.mlm.payment.paygl.R.color.white))
            txt_recharge_staus.setText("Your Recharge is "+rechargeStatus)
            txt_thanks.setText("OK THANKS")
            txt_thanks.setTextColor(resources.getColor(com.mlm.payment.paygl.R.color.green))
            img_status.setImageDrawable(resources.getDrawable(com.mlm.payment.paygl.R.drawable.ic_sucess))
            rl1.setBackgroundColor(resources.getColor(com.mlm.payment.paygl.R.color.green))

        }else if (rechargeStatus.equals("Failed")){
            txt_sucess.setText(rechargeStatus+"!")
            txt_sucess.setTextColor(resources.getColor(com.mlm.payment.paygl.R.color.white))
            txt_recharge_staus.setText("Your Recharge is "+rechargeStatus)
            txt_thanks.setText("Retry")
            txt_thanks.setTextColor(resources.getColor(com.mlm.payment.paygl.R.color.red))
            img_status.setImageDrawable(resources.getDrawable(com.mlm.payment.paygl.R.drawable.ic_faield))
            rl1.setBackgroundColor(resources.getColor(com.mlm.payment.paygl.R.color.red))
        }

        txt_thanks.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.window!!.setGravity(Gravity.CENTER)
        dialog?.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()



    }

}