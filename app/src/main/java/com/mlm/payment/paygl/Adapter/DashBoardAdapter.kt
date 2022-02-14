package com.mlm.payment.paygl.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mlm.payment.paygl.Pojo.Service
import com.mlm.payment.paygl.R
import com.mlm.payment.paygl.R.id.nav_host_fragment_content_dash_board
import com.pay.paygl.Helper.SessionManager


class DashBoardAdapter(var context: Context) : RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {
    var dataList = emptyList<Service>()
    private lateinit var mlistner: onItemClickedListner
    private lateinit var view: View

    internal fun setDataList(dataList: List<Service>) {
        this.dataList = dataList
    }

    interface onItemClickedListner {
        fun onItemclicked(position: Int,service: Service)
    }

    fun setOnItemClickListner(listner: onItemClickedListner) {
        mlistner = listner

    }

    class ViewHolder(itemView: View, listner: onItemClickedListner) : RecyclerView.ViewHolder(itemView) {
        var dashboardicon: ImageView
        var dashboardtextname: TextView
        var card_itemClicked: CardView

        init {
            dashboardicon = itemView.findViewById(R.id.dashboardicon)
            dashboardtextname = itemView.findViewById(R.id.dashboardtextname)
            card_itemClicked = itemView.findViewById(R.id.card_itemClicked)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardAdapter.ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item_layout, parent, false)
        return ViewHolder(view, mlistner)
    }

    override fun onBindViewHolder(holder: DashBoardAdapter.ViewHolder, position: Int) {
        var data = dataList[position]
        if (data.txtname != null) {
            holder.dashboardtextname.text = data.txtname
        }
        if (data.txtimg != null) {
            context?.let {
                Glide.with(it)
                    .load(data.txtimg)
                    .error(R.drawable.app_logo)
                    .placeholder(R.drawable.app_logo)
                    .into(holder.dashboardicon)
            }
        }

       holder. card_itemClicked.setOnClickListener {
            mlistner.onItemclicked(position,data)
        }
    }

    override fun getItemCount() = dataList.size

}
