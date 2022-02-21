package com.mlm.payment.paygl.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import com.mlm.payment.paygl.Pojo.ViewPlan
import com.mlm.payment.paygl.R


class OperatorPlanAdapter (val context: FragmentActivity?, var dataSource: List<ViewPlan>) : BaseAdapter() {

    private val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_plan_items, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSource.get(position).txtplandesc
        vh.tv_price.text = context?.getString(R.string.Rs)+" "+dataSource.get(position).txtplanamt
//        Log.e("data",""+ dataSource.get(position).txtopname)

        return view
    }

    override fun getItem(position: Int): ViewPlan {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }


    private class ItemHolder(row: View?) {
        val label: TextView
        val tv_price :TextView

        init {
            label = row?.findViewById(R.id.tv_data) as TextView
            tv_price = row?.findViewById(R.id.tv_price) as TextView
        }
    }

}