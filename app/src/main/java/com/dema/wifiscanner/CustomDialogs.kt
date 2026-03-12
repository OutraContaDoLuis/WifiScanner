package com.dema.wifiscanner

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup

class CustomDialogs() {

    companion object {

        fun loadingWifi(context: Context?) {
            val dialog = createDialog(context, R.layout.dialog_load_wifi)
        }

        fun showWifi() {

        }

        private fun createDialog(context: Context?, layout: Int): Dialog {
            val dialog = Dialog(context!!)
            dialog.setContentView(layout)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)

            return dialog
        }
    }

}