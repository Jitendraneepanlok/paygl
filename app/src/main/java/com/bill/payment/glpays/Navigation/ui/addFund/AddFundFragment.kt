package com.bill.payment.glpays.Navigation.ui.addFund

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bill.payment.glpays.Adapter.SummaryAdapter
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.AppSummaryModel
import com.bill.payment.glpays.Model.PayglXXXXXXXXXXXX
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.IncomeReport
import com.bill.payment.glpays.Pojo.SummaryPojo
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.FragmentAddFundBinding
import retrofit2.Call
import retrofit2.Response

class AddFundFragment : Fragment() {

    private lateinit var addFundModel: AddFundModel
    private var _binding: FragmentAddFundBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String
    lateinit var fundrecycler : RecyclerView
    private lateinit var adapter: SummaryAdapter
    private var summaryList = mutableListOf<IncomeReport>()
    
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

        getFundDetails()
        return root
    }

    private fun getFundDetails() {
        fundrecycler =  binding.fundrecycler
        var LayoutManager = LinearLayoutManager(activity,  LinearLayoutManager.VERTICAL, false)
        fundrecycler.layoutManager = LayoutManager

        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.getSummary(AppSummaryModel(PayglXXXXXXXXXXXX("1",user_id)))

        apiInterface.enqueue(object : retrofit2.Callback<SummaryPojo> {

            override fun onResponse(call: Call<SummaryPojo>, response: Response<SummaryPojo>) {

                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.GLPAYS?.response)
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT).show()
                    adapter = activity?.let { SummaryAdapter(it) }!!
                    fundrecycler.adapter = adapter
                    response.body()?.GLPAYS?.let {
                        summaryList.clear()
                        summaryList.addAll(it?.IncomeReport)
                        adapter.setsummaryList(summaryList)
                        adapter.notifyDataSetChanged()
                    }
                    adapter.setOnItemClickListner(object : SummaryAdapter.onItemClickedListner {
                        override fun onItemclicked(position: Int,incomeReport: IncomeReport) {
                            Log.e("itemPosition",""+position)

                            /*  dashboardId = service.txtid
                              val bundle = Bundle()
                              bundle.putString("DasgboardItemClicked_ID",dashboardId)*/

                        }
                    })
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<SummaryPojo>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })

    }

    override fun onStart() {
        super.onStart()
        navController = requireActivity().findNavController(R.id.nav_host_fragment_content_dash_board)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}