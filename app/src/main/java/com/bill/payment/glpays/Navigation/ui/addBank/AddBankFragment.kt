package com.bill.payment.glpays.Navigation.ui.addBank

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.PayglXXXXXX
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.AddBankDetailsResponse
import com.google.android.material.textfield.TextInputEditText
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.FragmentAddBankBinding
import retrofit2.Call
import retrofit2.Response

class AddBankFragment : Fragment() {
    private lateinit var addBankViewModel: AddBankModel
    private var _binding: FragmentAddBankBinding? = null
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    private lateinit var etname: TextInputEditText
    private lateinit var etifsc: TextInputEditText
    private lateinit var etbankname: TextInputEditText
    private lateinit var etbranchname: TextInputEditText
    private lateinit var etmobile: TextInputEditText
    private lateinit var etaccountno: TextInputEditText
    private lateinit var etpanno: TextInputEditText
    private lateinit var etadharno: TextInputEditText
    private lateinit var btnupdate: AppCompatButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addBankViewModel = ViewModelProvider(this).get(AddBankModel::class.java)

        _binding = FragmentAddBankBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sessionManager = SessionManager(activity)

        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)

        /*addBankViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        initView()
        return root
    }

    private fun initView() {
        etname = binding.etname
        etifsc = binding.etifsc
        etbankname = binding.etbankname
        etbranchname = binding.etbranchname
        etmobile = binding.etmobile
        etaccountno = binding.etaccountno
        etpanno = binding.etpanno
        etadharno = binding.etadharno
        btnupdate = binding.btnupdate
        btnupdate.setOnClickListener() {
            checkValidation()
        }

    }

    private fun checkValidation() {
        if (etname.text.toString().isEmpty()) {
            etname.setError("Please Enter Valid UserName");
            etname.requestFocus();
        } else if (etifsc.text.toString().isEmpty()) {
            etifsc.setError("Please Enter IFSC Code");
            etifsc.requestFocus();
        } else if (etbankname.text.toString().isEmpty()) {
            etbankname.setError("Please Enter Bank Name");
            etbankname.requestFocus();
        } else if (etbranchname.text.toString().isEmpty()) {
            etbranchname.setError("Please Enter Branch Name");
            etbranchname.requestFocus();
        } else if (etmobile.text.toString().isEmpty()) {
            etmobile.setError("Please Enter Mobile No.");
            etmobile.requestFocus();
        } else if (etaccountno.text.toString().isEmpty()) {
            etaccountno.setError("Please Enter Account No.");
            etaccountno.requestFocus();
        } else if (etpanno.text.toString().isEmpty()) {
            etpanno.setError("Please Enter Pan No.");
            etpanno.requestFocus();
        } else if (etadharno.text.toString().isEmpty()) {
            etadharno.setError("Please Enter Adhar No.");
            etadharno.requestFocus();
        } else {
            CallApi()
        }

    }

    private fun CallApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();
        val apiInterface = ApiClient.getClient.AddBank(
            com.bill.payment.glpays.Model.AddBankModel(
                PayglXXXXXX(
                    etadharno.text.toString(),
                    etaccountno.text.toString(),
                    etbankname.text.toString(),
                    etbranchname.text.toString(),
                    etmobile.text.toString(),
                    etifsc.text.toString(),
                    etname.text.toString(),
                    etpanno.text.toString(),
                    user_id
                )
            )
        )

        apiInterface.enqueue(object : retrofit2.Callback<AddBankDetailsResponse> {

            override fun onResponse(
                call: Call<AddBankDetailsResponse>,
                response: Response<AddBankDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("BankResponse", "" + response.body()?.Paygl?.response)
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.Paygl?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AddBankDetailsResponse>, t: Throwable) {
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
}