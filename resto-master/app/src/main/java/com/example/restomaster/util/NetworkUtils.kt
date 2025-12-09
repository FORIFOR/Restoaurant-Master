package com.example.restomaster.util

import java.net.Inet4Address
import java.net.NetworkInterface

object NetworkUtils {
    fun getIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            val addresses = ArrayList<String>()

            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                // Skip loopback and down interfaces
                if (networkInterface.isLoopback || !networkInterface.isUp) continue

                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val address = inetAddresses.nextElement()
                    // We only care about IPv4
                    if (address is Inet4Address && !address.isLoopbackAddress) {
                        val ip = address.hostAddress
                        if (ip != null) {
                            addresses.add(ip)
                        }
                    }
                }
            }

            // Priority 1: Android Hotspot default
            addresses.firstOrNull { it.startsWith("192.168.43.") }?.let { return it }

            // Priority 2: Standard local networks (192.168.x.x)
            addresses.firstOrNull { it.startsWith("192.168.") }?.let { return it }

            // Priority 3: Private Class B (172.16.x.x - 172.31.x.x)
            addresses.firstOrNull { 
                val parts = it.split(".")
                if (parts.size == 4) {
                    val second = parts[1].toIntOrNull() ?: 0
                    parts[0] == "172" && second in 16..31
                } else false
            }?.let { return it }

            // Priority 4: Private Class A (10.x.x.x)
            addresses.firstOrNull { it.startsWith("10.") }?.let { return it }

            // Fallback: Return the first found non-loopback IPv4
            return addresses.firstOrNull()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
