package com.bill.payment.glpays.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bill.payment.glpays.Pojo.IncomeReport
import com.bill.payment.glpays.R

class SummaryAdapter(var context: Context) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {
    var summaryList = emptyList<IncomeReport>()
    private lateinit var mlistner: onItemClickedListner
    private lateinit var view: View

    internal fun setsummaryList(summaryList: List<IncomeReport>) {
        this.summaryList = summaryList
    }

    interface onItemClickedListner {
        fun onItemclicked(position: Int, incomeReport: IncomeReport)
    }

    fun setOnItemClickListner(listner: onItemClickedListner) {
        mlistner = listner

    }

    class ViewHolder(itemView: View, listner: onItemClickedListner) :
        RecyclerView.ViewHolder(itemView) {
        var tvuservalue: AppCompatTextView
        var tvtransaction_id: AppCompatTextView
        var tvdescription: AppCompatTextView
        var tvcreditvalue: AppCompatTextView
        var tvdebittvalue: AppCompatTextView
        var tvoperatorvalue: AppCompatTextView
        var tvdecuvalue: AppCompatTextView
        var tvdatevalue: AppCompatTextView

        init {
            tvuservalue = itemView.findViewById(R.id.tvuservalue)
            tvtransaction_id = itemView.findViewById(R.id.tvtransaction_id)
            tvdescription = itemView.findViewById(R.id.tvdescription)
            tvcreditvalue = itemView.findViewById(R.id.tvcreditvalue)
            tvdebittvalue = itemView.findViewById(R.id.tvdebittvalue)
            tvoperatorvalue = itemView.findViewById(R.id.tvoperatorvalue)
            tvdecuvalue = itemView.findViewById(R.id.tvdecuvalue)
            tvdatevalue = itemView.findViewById(R.id.tvdatevalue)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryAdapter.ViewHolder {
        view =
            LayoutInflater.from(parent.context).inflate(R.layout.summary_item_layout, parent, false)
        return ViewHolder(view, mlistner)
    }

    override fun onBindViewHolder(holder: SummaryAdapter.ViewHolder, position: Int) {
        var data = summaryList[position]

        if (data.txtuser != null) {
            holder.tvuservalue.text = data.txtuser
        }

        if (data.txttnsid != null) {
            holder.tvtransaction_id.text = data.txttnsid

        }
        if (data.txtDesc != null) {
            holder.tvdescription.text = data.txtDesc

        }
        if (data.txtcr != null) {
            holder.tvcreditvalue.text = data.txtcr

        }
        if (data.txtdr != null) {
            holder.tvdebittvalue.text = data.txtdr
        }

        if (data.txtopcode != null) {
            holder.tvoperatorvalue.text = data.txtopcode
        }

        if (data.txtdecu != null) {
            holder.tvdecuvalue.text = data.txtdecu
        }
        if (data.txtdate != null) {
            holder.tvdatevalue.text = data.txtdate
        }

    }

    override fun getItemCount() = summaryList.size

}
