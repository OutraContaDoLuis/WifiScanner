package com.dema.wifiscanner.model

data class WifiItemModel(
    var ssid: String? = "",
    var connectionPrivate: Boolean? = false,
    var macAddress: String? = ""
)