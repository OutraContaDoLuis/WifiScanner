package com.dema.wifiscanner.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dema.wifiscanner.CustomDialogs
import com.dema.wifiscanner.R
import com.dema.wifiscanner.model.WifiItemModel
import com.dema.wifiscanner.fragment.ListWifiFragment

class HomeActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var btnScanWifi: Button

    private val permissionGranted = PackageManager.PERMISSION_GRANTED
    private val permissionAccessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionAccessCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    private val permissionChangeWifiState = Manifest.permission.ACCESS_FINE_LOCATION

    private val tag = javaClass.name
    private val tagWifiManager = "TagWifiManager"

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
        CustomDialogs.Companion.loadingWifi(this)
    }

    private fun getConnections() {

        val accessFineLocationGranted = (
                ContextCompat.checkSelfPermission(this, permissionAccessFineLocation)
            == permissionGranted)
        val accessCoarseLocationGranted = (
                ContextCompat.checkSelfPermission(this, permissionAccessCoarseLocation)
                        == permissionGranted)
        val changeWifiStateGranted = (
                ContextCompat.checkSelfPermission(this, permissionChangeWifiState)
                        == permissionGranted)

        if (!accessFineLocationGranted || !accessCoarseLocationGranted || !changeWifiStateGranted) {
            Log.v(tagWifiManager, "No permissions granted!")
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    permissionAccessFineLocation,
                    permissionAccessCoarseLocation,
                    permissionChangeWifiState),
                LOCATION_PERMISSION_REQUEST_CODE)
        }

        Log.v(tagWifiManager, accessFineLocationGranted.toString())
        Log.v(tagWifiManager, accessCoarseLocationGranted.toString())
        Log.v(tagWifiManager, changeWifiStateGranted.toString())

        val wifiManager = this.getSystemService(WIFI_SERVICE) as WifiManager

        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    Log.v(tagWifiManager, "other results: ${wifiManager.scanResults}")

                    val listWifiItemModel: ArrayList<WifiItemModel?> = arrayListOf()

                    wifiManager.scanResults.forEach { it ->
                        Log.v(tagWifiManager, "Result: $it")

                        val wifiSsid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            it.wifiSsid.toString()
                        } else {
                            it.SSID
                        }

                        val wifiConnectionPrivate = it.capabilities.contains("WPA2") ||
                            it.capabilities.contains("WPA")

                        val wifiBssid = it.BSSID
                        val capabilities = it.capabilities

                        val wifiItemModel = WifiItemModel(
                            wifiSsid,
                            wifiConnectionPrivate,
                            wifiBssid,
                            capabilities
                        )

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