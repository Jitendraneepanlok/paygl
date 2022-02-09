package com.pay.paygl.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mlm.payment.paygl.Navigation.DashBoardActivity
import com.mlm.payment.paygl.R
import com.pay.paygl.Helper.SessionManager
import com.pay.paygl.Model.LoginModel
import com.pay.paygl.Model.Paygl
import com.pay.paygl.Network.ApiClient
import com.pay.paygl.Pojo.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var ischecked = false
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        initView()
    }

    private fun initView() {
        val cb_remember: AppCompatCheckBox = findViewById(R.id.cb_remember)
        cb_remember.setOnCheckedChangeListener { buttonView, isChecked ->
            ischecked = isChecked
        }

        img_back.setOnClickListener() {
            startActivity(Intent(this, WelcomeSliderActivity::class.java))

        }

        txt_forget.setOnClickListener() {
            startActivity(Intent(this, ForgotActivity::class.java))

        }

        btn_continue.setOnClickListener() {
           checkValidations()
            //startActivity(Intent(this, DashBoardActivity::class.java))
        }
    }

    private fun checkValidations() {

        val et_name: AppCompatEditText = findViewById(R.id.et_name);
        val et_password: AppCompatEditText = findViewById(R.id.et_password)

        if (et_name.text.toString().isEmpty()) {
            et_name.setError("Please Enter Valid UserName");
            et_name.requestFocus();
        } else if (et_password.text.toString().isEmpty()) {
            et_password.setError("Please Enter Valid Password");
            et_password.requestFocus();
        } else {
            CallLoginApi()
        }
    }

    private fun CallLoginApi() {
        val pDialog = ProgressDialog(this@LoginActivity)
        pDialog.setMessage(application?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();


        val apiInterface = ApiClient.getClient.PostLogin(
            LoginModel(
                Paygl(
                    et_name.text.toString(),
                    et_password.text.toString()
                )
            )
        )

        apiInterface.enqueue(object : retrofit2.Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    if (response.body()?.Paygl?.resMessage.equals("1")) {
                        Log.e("Response", "" + response.body()?.Paygl?.response)
                        Toast.makeText(
                            applicationContext,
                            response.body()?.Paygl?.response,
                            Toast.LENGTH_SHORT
                        ).show()
                        sessionManager = SessionManager(applicationContext)
                        sessionManager.setValueBoolean(SessionManager.VALUE, ischecked)
                        sessionManager.setValue(
                            SessionManager.User_Id,
                            response.body()?.Paygl?.txtuserID
                        )
                        sessionManager.setValue(
                            SessionManager.Login_Id,
                            response.body()?.Paygl?.txtLoginID
                        )
                        /* sessionManager?.setValue(sessionManager.Joining_date,response.body()?.Paygl?.txtjoindate)
                      sessionManager?.setValue(sessionManager.Holder_Name,response.body()?.Paygl?.txtholdername)
                      sessionManager?.setValue(sessionManager.Bank_Id,response.body()?.Paygl?.txtbankid)
                      sessionManager?.setValue(sessionManager.User_NAME, response.body()?.Paygl?.txtname)
                      sessionManager?.setValue(sessionManager.User_Password, response.body()?.Paygl?.txtpass)
                      sessionManager?.setValue(sessionManager.Email_Id, response.body()?.Paygl?.txtemail)
                      sessionManager?.setValue(sessionManager.Mobile_no, response.body()?.Paygl?.txtmobile)*/
                        callnewPage()
                        pDialog.dismiss()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            response.body()?.Paygl?.response,
                            Toast.LENGTH_SHORT
                        ).show()
                        pDialog.dismiss()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    ).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun callnewPage() {
        startActivity(Intent(this, DashBoardActivity::class.java))
    }
}
