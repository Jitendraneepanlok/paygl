package com.bill.payment.glpays.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bill.payment.glpays.Pojo.Operator
import com.bill.payment.glpays.R

class PlanAdapter(val context: FragmentActivity?, var dataSource: List<Operator>) : BaseAdapter() {

    private val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_items, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }

        if (position==0){
            vh.label.setText("Select Operator")
        }else {
            vh.label.text = dataSource.get(position).txtopname
            Log.e("data", "" + dataSource.get(position).txtopname)
        }


        return view
    }

    override fun getItem(position: Int): Operator{
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

        init {
            label = row?.findViewById(R.id.tv_data) as TextView
        }
    }

}