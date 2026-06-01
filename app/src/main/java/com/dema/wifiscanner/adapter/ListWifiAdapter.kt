package com.dema.wifiscanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
        val ssidSb = StringBuilder()
        ssidSb.append("SSID: ")
        ssidSb.append(item?.ssid)
        txtSsid.text = ssidSb.toString()

        val txtConnectionPrivate = itemView.findViewById<TextView>(R.id.txt_connection_private)
        val connectionPrivateSb = StringBuilder()
        connectionPrivateSb.append("<b>Conexão: </b>")
        if (item?.connectionPrivate == true) {
            connectionPrivateSb.append("Privada")
        } else {
            connectionPrivateSb.append("Pública")
        }

        val connectionPrivateHtml =
            HtmlCompat.fromHtml(connectionPrivateSb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        txtConnectionPrivate.text = connectionPrivateHtml

        val txtMecAddress = itemView.findViewById<TextView>(R.id.txt_mac_address)
        val mecAddressSb = StringBuilder()
        mecAddressSb.append("<b>Endereço MAC: </b>")
        mecAddressSb.append(item?.macAddress)

        val mecAddressHtml =
            HtmlCompat.fromHtml(mecAddressSb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        txtMecAddress.text = mecAddressHtml

        return itemView
    }
}