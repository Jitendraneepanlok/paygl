package com.bill.payment.glpays.Navigation.ui.slideshow

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.ChangePassModel
import com.bill.payment.glpays.Model.PayglXXXX
import com.bill.payment.glpays.Model.PayglXXXXXXX
import com.bill.payment.glpays.Model.UserDetailsModel
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.ChangePasswordResponse
import com.bill.payment.glpays.Pojo.UserDetailsResponse
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.FragmentSlideshowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    lateinit var oldpassword: String
    lateinit var txtnpassword: String



    private lateinit var btncontinue: AppCompatButton
    lateinit var etpass1: AppCompatEditText
    lateinit var etconfirmpass: AppCompatEditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

       /* val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)

        getUserDetailsApi()

        initView()
        return root
    }

    private fun initView() {
        btncontinue =binding.btnContinue
        btncontinue.setOnClickListener() {
            CheckValidation()
        }
    }


    private fun getUserDetailsApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface =
            ApiClient.getClient.getUserDetails(UserDetailsModel(PayglXXXX(user_id/*"1"*/)))

        apiInterface.enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(
                call: Call<UserDetailsResponse>,
                response: Response<UserDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    pDialog.dismiss()
                    Log.e("Response", "" + response.body()?.Paygl?.response)
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    if (response.body()?.Paygl?.txtpass != null) {
                        oldpassword = response.body()?.Paygl?.txtpass.toString()
                    }

                    if (response.body()?.Paygl?.txttxnpass != null) {
                        txtnpassword = response.body()?.Paygl?.txttxnpass.toString()
                    }


                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
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

    private fun CheckValidation() {
        etconfirmpass = binding.etconfirmpass
        etpass1 = binding.etpass1

        if (etpass1.text.toString().isEmpty()){
            etpass1.requestFocus()
            etpass1.setError("Field should not be empty")
        }else if (etconfirmpass.text.toString().isEmpty()){
            etconfirmpass.requestFocus()
            etconfirmpass.setError("Field should not be empty")
        }else{
            CheckValue()
        }
    }

    private fun CheckValue() {
        val result = oldpassword.equals(etpass1.text.toString())
        if (result) {
            Log.e("valuefinal", "true")
            callApi()
        } else {
            Log.e("valuefinal", "false")
            Toast.makeText(activity, "Old Paasowrd does not Match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.ChangePass(ChangePassModel(PayglXXXXXXX(etpass1.text.toString(), txtnpassword, user_id)))

        apiInterface.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if (response.isSuccessful) {
                    pDialog.dismiss()
                    Log.e("Login Response", "" + response.body()?.Paygl?.response)
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}