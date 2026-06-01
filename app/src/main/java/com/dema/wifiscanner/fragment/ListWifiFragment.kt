package com.dema.wifiscanner.fragment

import android.net.wifi.ScanResult
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.dema.wifiscanner.CustomDialogs
import com.dema.wifiscanner.adapter.ListWifiAdapter
import com.dema.wifiscanner.R
import com.dema.wifiscanner.model.WifiItemModel

class ListWifiFragment(listWifiItemModel: ArrayList<WifiItemModel?>) : Fragment() {

    private var listWifiItemModel: ArrayList<WifiItemModel?> = arrayListOf()

    init {
        this.listWifiItemModel = listWifiItemModel
    }

    private lateinit var listViewWifi: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_wifi, container, false)

        listViewWifi = view.findViewById(R.id.list_view_wifi)
        val adapter = ListWifiAdapter(requireContext(), listWifiItemModel)
        listViewWifi.adapter = adapter
        listViewWifi.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            CustomDialogs.wifiDetails(requireContext(), listWifiItemModel[i])
        }

        return view
    }
}