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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mlm.payment.paygl.Pojo.Service
import com.mlm.payment.paygl.R
import com.pay.paygl.Helper.SessionManager


class DashBoardAdapter(var context: Context) : RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {

    var dataList = emptyList<Service>()
    private lateinit var mlistner: onItemClickedListner
    private lateinit var view: View
    private lateinit var sessionManager: SessionManager


    internal fun setDataList(dataList: List<Service>) {
        this.dataList = dataList
    }

    // item clicked Listers

    interface onItemClickedListner {

        fun onItemclicked(position: Int)


    }

    fun setOnItemClickListner(listner: onItemClickedListner) {
        mlistner = listner

    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View, listner: onItemClickedListner) :
        RecyclerView.ViewHolder(itemView) {
        var dashboardicon: ImageView
        var dashboardtextname: TextView
        var card_itemClicked: CardView

        init {
            dashboardicon = itemView.findViewById(R.id.dashboardicon)
            dashboardtextname = itemView.findViewById(R.id.dashboardtextname)
            card_itemClicked = itemView.findViewById(R.id.card_itemClicked)
            itemView.setOnClickListener {
                listner.onItemclicked(adapterPosition)
            }
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardAdapter.ViewHolder {
        // Inflate the custom layout
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_item_layout, parent, false)
        return ViewHolder(view, mlistner)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: DashBoardAdapter.ViewHolder, position: Int) {

        // Get the data model based on position
        var data = dataList[position]

        // Set item views based on your views and data model
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




        holder.card_itemClicked.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (data.txtid != null) {
                    val aa: String = data.txtid
                    sessionManager = SessionManager(context)
                    sessionManager.setValue(SessionManager.txtid, aa)
                }
            }
        })

    }

    //  total count of items in the list
    override fun getItemCount() = dataList.size

}
