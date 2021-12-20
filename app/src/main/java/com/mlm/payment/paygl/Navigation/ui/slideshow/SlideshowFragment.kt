package com.mlm.payment.paygl.Navigation.ui.slideshow

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
import com.mlm.payment.paygl.Pojo.UserDetailsResponse
import com.mlm.payment.paygl.R
import com.mlm.payment.paygl.databinding.FragmentSlideshowBinding
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Model.ChangePassModel
import com.pay.paygl.Model.PayglXXXX
import com.pay.paygl.Model.PayglXXXXXXX
import com.pay.paygl.Model.UserDetailsModel
import com.pay.paygl.Network.ApiClient
import com.pay.paygl.Pojo.ChangePasswordResponse
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

    lateinit var et_old_pass_1: String
    lateinit var et_old_pass_2: String
    lateinit var et_old_pass_3: String
    lateinit var et_old_pass_4: String
    lateinit var result: String

    lateinit var new_pass_1: String
    lateinit var new_pass_2: String
    lateinit var new_pass_3: String
    lateinit var new_pass_4: String

    private lateinit var btncontinue: AppCompatButton
    lateinit var etpass1: AppCompatEditText
    lateinit var etpass2: AppCompatEditText
    lateinit var etpass3: AppCompatEditText
    lateinit var etpass4: AppCompatEditText
    lateinit var etnewpass1: AppCompatEditText
    lateinit var etnewpass2: AppCompatEditText
    lateinit var etnewpass3: AppCompatEditText
    lateinit var etnewpass4: AppCompatEditText

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


        etpass1 = binding.etpass1
        etpass2 = binding.etpass2
        etpass3 = binding.etpass3
        etpass4 = binding.etpass4

        etnewpass1 = binding.etnewpass1
        etnewpass2 = binding.etnewpass2
        etnewpass3 = binding.etnewpass3
        etnewpass4 = binding.etnewpass4


        et_old_pass_1 = etpass1.text.toString()
        et_old_pass_2 = etpass2.text.toString()
        et_old_pass_3 = etpass3.text.toString()
        et_old_pass_4 = etpass4.text.toString()

        new_pass_1 = etnewpass1.text.toString()
        new_pass_2 = etnewpass2.text.toString()
        new_pass_3 = etnewpass3.text.toString()
        new_pass_4 = etnewpass4.text.toString()


        if (!et_old_pass_1.equals("")) {
            if (!et_old_pass_2.equals("")) {
                if (!et_old_pass_3.equals("")) {
                    if (!et_old_pass_4.equals("")) {
                        if (!new_pass_1.equals("")) {
                            if (!new_pass_2.equals("")) {
                                if (!new_pass_3.equals("")) {
                                    if (!new_pass_4.equals("")) {
                                        CheckValue()
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "Field should be not empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Field should be not empty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Field should be not empty",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                activity,
                                "Field should be not empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(activity, "Field should be not empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Field should be not empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Field should be not empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "Field should be not empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun CheckValue() {
        val finalvalue = et_old_pass_1 + et_old_pass_2 + et_old_pass_3 + et_old_pass_4

        val result = oldpassword.equals(finalvalue)

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

        val apiInterface = ApiClient.getClient.ChangePass(
            ChangePassModel(
                PayglXXXXXXX(
                    new_pass_1 + new_pass_2 + new_pass_3 + new_pass_4,
                    txtnpassword,
                    user_id
                )
            )
        )

        apiInterface.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
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