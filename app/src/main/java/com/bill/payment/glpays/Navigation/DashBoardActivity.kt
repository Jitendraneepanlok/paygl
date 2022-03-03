package com.bill.payment.glpays.Navigation

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.bill.payment.glpays.Activity.LoginActivity
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.LogoutModel
import com.bill.payment.glpays.Model.PayglXX
import com.bill.payment.glpays.Model.PayglXXXX
import com.bill.payment.glpays.Model.UserDetailsModel
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.LogoutResponse
import com.bill.payment.glpays.Pojo.UserDetailsResponse
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.ActivityDashBoardBinding
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_dash_board.view.*
import kotlinx.android.synthetic.main.nav_header_dash_board.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashBoardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashBoardBinding
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    lateinit var headerView: View
    lateinit var tvprofilename: TextView
    lateinit var tvprofileEmail: TextView
    lateinit var imageView: CircleImageView
    private lateinit var imgwalletbalance : AppCompatImageView
   lateinit var toolbar : androidx.appcompat.widget.Toolbar
   private lateinit var walletavailableamount :String
   private lateinit var ProfileName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(applicationContext,R.color.purple_500));
        }
        sessionManager = SessionManager(applicationContext)


        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarDashBoard.toolbar)

         toolbar = binding.root.toolbar
        imgwalletbalance = toolbar.imgwalletbalance
        imgwalletbalance.setOnClickListener(){
            OpenWalletDialog()
        }

        if (sessionManager.getUserData(SessionManager.User_Id)!=null) {
            user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
            Log.e("Value", "" + user_id)
        }
        getUserDetails()
        /* binding.appBarDashBoard.fab.setOnClickListener { view ->
             Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show()
         }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navView: NavigationView = binding.navView
        headerView = navView.getHeaderView(0);
        tvprofilename = headerView.tvprofilename
        tvprofileEmail = headerView.tvprofileEmail
        imageView = headerView.imageView
        val navController = findNavController(R.id.nav_host_fragment_content_dash_board)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navController.navigate(R.id.nav_home)

                R.id.nav_logout -> CallLogout()

                R.id.nav_about_us -> callAboutUs()

                R.id.nav_welcome_letter ->navController.navigate(R.id.nav_welcome_letter)

            }
            NavigationUI.onNavDestinationSelected(item, navController)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        })

    }

    private fun OpenWalletDialog() {
        val dialog = this?.let { Dialog(it, R.style.DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.wallet_layout)
        dialog?.setCancelable(true)


        val txt_cancel: AppCompatTextView = dialog!!.findViewById(R.id.txt_cancel)
        txt_cancel.setOnClickListener {
            dialog.dismiss()
        }

        if (sessionManager.getUserData(SessionManager.POST)!=null) {
            val txt_name: AppCompatTextView = dialog!!.findViewById(R.id.txt_name)
            txt_name.setText(ProfileName + "(" + sessionManager.getUserData(SessionManager.POST) + ")")
        }

        if (sessionManager.getUserData(SessionManager.CREDIT_AMOUNT)!=null) {
            val txt_credit_fund_balance: AppCompatTextView = dialog!!.findViewById(R.id.txt_credit_fund_balance)
            txt_credit_fund_balance.setText(this.getString(R.string.Rs) + " " + sessionManager.getUserData(SessionManager.CREDIT_AMOUNT))
        }

        if (sessionManager.getUserData(SessionManager.COMMISION)!=null) {
            val txt_total_commission: AppCompatTextView = dialog!!.findViewById(R.id.txt_total_commission)
            txt_total_commission.setText(this.getString(R.string.Rs) + " " + sessionManager.getUserData(SessionManager.COMMISION))
        }

        if (sessionManager.getUserData(SessionManager.OPEN_BALANCE)!=null) {
            val tv_available_opening_balance: AppCompatTextView =
                dialog!!.findViewById(R.id.tv_available_opening_balance)
            tv_available_opening_balance.setText(
                this.getString(R.string.Rs) + " " + sessionManager.getUserData(
                    SessionManager.OPEN_BALANCE
                )
            )

        }

        if (sessionManager.getUserData(SessionManager.TOTAL_AMOUNT)!=null) {
            val tv_available_balance: AppCompatTextView =
                dialog!!.findViewById(R.id.tv_available_balance)
            tv_available_balance.setText(
                this.getString(R.string.Rs) + " " + sessionManager.getUserData(
                    SessionManager.TOTAL_AMOUNT
                )/*walletavailableamount*/
            )
        }
        dialog?.window!!.setGravity(Gravity.TOP)
        dialog?.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun callAboutUs() {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://glpays.com/")
        startActivity(openURL)
    }

    private fun CallLogout() {
        val dialog = this?.let { Dialog(it, R.style.DialogTheme) }
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
        val pDialog = ProgressDialog(this)
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
                        applicationContext,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    ).show()

                    callnewPage()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    ).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun callnewPage() {
        sessionManager.logout()
        startActivity(Intent(applicationContext, LoginActivity::class.java))

    }

    private fun getUserDetails() {
        val pDialog = ProgressDialog(this)
        pDialog.setMessage(this?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getUserDetails(UserDetailsModel(PayglXXXX(user_id/*"1"*/)))

        apiInterface.enqueue(object : Callback<UserDetailsResponse> {

            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {

                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.Paygl?.response)

                    ProfileName = response.body()?.Paygl?.txtname.toString()

                    if (response.body()?.Paygl?.txtname != null) {
                        tvprofilename.setText(response.body()?.Paygl?.txtname +"("+sessionManager.getUserData(SessionManager.POST)+")")

                    }
                    if (response.body()?.Paygl?.txtemail != null) {
                        tvprofileEmail.setText(response.body()?.Paygl?.txtemail)
                    }

                    if (response.body()?.Paygl?.txttotalamt != null) {
                        Log.e("txttotalamt", "" +response.body()?.Paygl?.txttotalamt)
                        walletavailableamount = response.body()?.Paygl?.txttotalamt.toString()

                    }

                    val media = response.body()?.Paygl?.txtphoto
                    if (media !== null) {
                        applicationContext?.let {
                            Glide.with(it)
                                .load(media)
                                .centerCrop()
                                .placeholder(R.drawable.app_logo)
                                .error(R.drawable.app_logo)
                                .into(imageView)

                        }
                    } else {

                        imageView.setImageResource(R.drawable.user)
                        pDialog.dismiss()
                    }
                    pDialog.dismiss()

                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.Paygl?.response,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dash_board, menu)
        return true
    }
*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dash_board)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


   /* override fun onBackPressed() {
        super.onBackPressed()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)

    }*/
}