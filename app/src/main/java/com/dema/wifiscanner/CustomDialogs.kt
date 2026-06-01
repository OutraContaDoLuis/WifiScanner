package com.dema.wifiscanner

import android.app.Dialog
import android.content.Context
import android.net.wifi.ScanResult
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.dema.wifiscanner.model.WifiItemModel

class CustomDialogs() {

    companion object {

        fun loadingWifi(context: Context?) {
            val dialog = createDialog(context, R.layout.dialog_load_wifi)
        }

        fun showWifi() {

        }

        fun wifiDetails(context: Context?, wifiItemModel: WifiItemModel?) {
            val dialog = createDialog(context, R.layout.dialog_wifi_details)

            val txtSsid = dialog.findViewById<TextView>(R.id.txt_ssid)
            val ssidSb = StringBuilder()
            ssidSb.append("SSID: ")
            ssidSb.append(wifiItemModel?.ssid)
            txtSsid.text = ssidSb

            val txtConnectionType = dialog.findViewById<TextView>(R.id.txt_connection_type)
            val connectionTypeSb = StringBuilder()
            connectionTypeSb.append("Tipo de conexão: ")
            val privateConnection = if (wifiItemModel?.connectionPrivate == true) {
                "Privada"
            } else {
                "Pública"
            }
            connectionTypeSb.append(privateConnection)
            txtConnectionType.text = connectionTypeSb

            val txtBssid = dialog.findViewById<TextView>(R.id.txt_bssid)
            val bssidSb = StringBuilder()
            bssidSb.append("Bssid (MAC): ")
            bssidSb.append(wifiItemModel?.macAddress)
            txtBssid.text = bssidSb

            val txtProtocols = dialog.findViewById<TextView>(R.id.txt_protocols)
            val protocolsSb = StringBuilder()
            protocolsSb.append("Protocolos: ")
            protocolsSb.append(wifiItemModel?.capabilities)
            txtProtocols.text = protocolsSb

            val txtSecurityLevel = dialog.findViewById<TextView>(R.id.txt_security_level)
            val securityLevelSb = StringBuilder()
            securityLevelSb.append("Nível de segurança: ")
            txtSecurityLevel.text = securityLevelSb

            val btnBreakPassword = dialog.findViewById<Button>(R.id.btn_break_password)

            dialog.setCancelable(true)

            dialog.show()
        }

        private fun createDialog(context: Context?, layout: Int): Dialog {
            val dialog = Dialog(context!!)
            dialog.setContentView(layout)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)

            return dialog
        }
    }

}