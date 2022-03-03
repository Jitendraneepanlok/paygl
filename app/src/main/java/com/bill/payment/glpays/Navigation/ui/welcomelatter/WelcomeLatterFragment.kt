package com.bill.payment.glpays.Navigation.ui.welcomelatter

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.PayglXXXX
import com.bill.payment.glpays.Model.UserDetailsModel
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.UserDetailsResponse
import com.bill.payment.glpays.databinding.FragmentWelcomeLetterBinding
import retrofit2.Call
import retrofit2.Response

class WelcomeLatterFragment : Fragment() {

    private lateinit var welcomeLatterModel: WelcomeLatterModel
    private var _binding: FragmentWelcomeLetterBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var user_id: String

    private lateinit var txtAssociateid: AppCompatTextView
    private lateinit var txtAssociateName: AppCompatTextView
    private lateinit var tvjoiningdate: AppCompatTextView
    private lateinit var tvpincode: AppCompatTextView
    private lateinit var tvaddress: AppCompatTextView
    private lateinit var tvsponserid: AppCompatTextView
    private lateinit var tvsponsername: AppCompatTextView
    private lateinit var tvammount: AppCompatTextView
    private lateinit var tvpackage: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        welcomeLatterModel = ViewModelProvider(this).get(WelcomeLatterModel::class.java)
        _binding = FragmentWelcomeLetterBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(activity)
        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        val root: View = binding.root
        /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/

        initView()
        CallgetUserDetailsApi()
        return root
    }

    private fun initView() {

        txtAssociateid = binding.txtAssociateid
        txtAssociateName = binding.txtAssociateName
        tvjoiningdate = binding.tvjoiningdate
        tvpincode = binding.tvpincode
        tvaddress = binding.tvaddress
        tvsponserid = binding.tvsponserid
        tvsponsername = binding.tvsponsername
        tvammount = binding.tvammount
        tvpackage = binding.tvpackage

    }


    private fun CallgetUserDetailsApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(this?.getString(com.bill.payment.glpays.R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getUserDetails(UserDetailsModel(PayglXXXX(/*user_id*/"1")))

        apiInterface.enqueue(object : retrofit2.Callback<UserDetailsResponse> {

            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)
//                    Toast.makeText(applicationContext, response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()

                    if (response.body()?.Paygl?.txtname != null) {
                        txtAssociateName.setText("Associate Name :  "+response.body()?.Paygl?.txtname)

                    }
                    if (response.body()?.Paygl?. txtLoginID!= null) {
                        txtAssociateid.setText("Associate ID :  "+response.body()?.Paygl?.txtemail)
                    }

                    if (response.body()?.Paygl?. txtjoindate!= null) {
                        tvjoiningdate.setText("Joining Date :  "+response.body()?.Paygl?.txtemail)
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

}