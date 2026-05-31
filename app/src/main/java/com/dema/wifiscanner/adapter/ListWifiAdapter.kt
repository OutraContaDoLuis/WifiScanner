package com.dema.wifiscanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dema.wifiscanner.R
import com.dema.wifiscanner.model.WifiItemModel

class ListWifiAdapter(context: Context, listWifiItemModel: ArrayList<WifiItemModel?>)
    : ArrayAdapter<WifiItemModel?>(context, 0, listWifiItemModel) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val item = getItem(position)

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.wifi_item, parent, false)
        }

        val txtSsid = itemView.findViewById<TextView>(R.id.txt_ssid)
        txtSsid.text = item?.ssid

        return itemView
    }
}