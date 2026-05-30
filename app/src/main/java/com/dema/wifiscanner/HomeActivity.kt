package com.dema.wifiscanner

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.telecom.Connection
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import java.security.Permission

class HomeActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var btnScanWifi: Button

    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnScanWifi = findViewById(R.id.btn_scan_wifi)
        btnScanWifi.setOnClickListener { scanWifi() }

        getConnections()
    }

    private fun scanWifi() {
        CustomDialogs.loadingWifi(this)
    }

    private fun getConnections() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.v("Luis", "Sem permissão!")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CHANGE_WIFI_STATE),
                LOCATION_PERMISSION_REQUEST_CODE)
        }

        Log.v("Luis", (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED).toString())
        Log.v("Luis", (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED).toString())
        Log.v("Luis", (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED).toString())

        val wifiManager = this.getSystemService(WIFI_SERVICE) as WifiManager

        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    Log.v("Luis", "other results: ${wifiManager.scanResults}")

                    val listWifiItemModel: ArrayList<WifiItemModel?> = arrayListOf()

                    wifiManager.scanResults.forEach { it ->
                        Log.v("Luis", "Result: $it")

                        val wifiItemModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            WifiItemModel(it.wifiSsid.toString())
                        } else {
                            WifiItemModel(it.SSID.toString())
                        }

                        var haveAlreadyThisSsid = false

                        listWifiItemModel.forEach { it ->
                            if (it?.ssid.toString() == wifiItemModel.ssid) {
                                haveAlreadyThisSsid = true
                                return@forEach
                            }
                        }

                        if (haveAlreadyThisSsid) return@forEach

                        listWifiItemModel.add(wifiItemModel)
                    }

                    val listWifiFragment = ListWifiFragment(listWifiItemModel)
                    replaceFragment(listWifiFragment)
                } else {
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        this.registerReceiver(wifiScanReceiver, intentFilter)

        wifiManager.startScan()
//
//        if (success) {
//            val results = wifiManager.scanResults
//            Log.v("Luis", "Sucesso!")
//            Log.v("Luis", results.toString())
//        } else {
//            val results = wifiManager.scanResults
//            Log.v("Luis", "Sem sucesso!")
//            Log.v("Luis", wifiManager.wifiState.toString())
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getConnections()
            } else {
                // Permission denied. Handle the denial as needed.
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.home_fragment, fragment).commit()
    }
}