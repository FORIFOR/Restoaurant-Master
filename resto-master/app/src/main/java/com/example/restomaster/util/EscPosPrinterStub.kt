package com.example.restomaster.util

import android.util.Log

/**
 * Placeholder for ESC/POS送出。実装時はBluetooth/LANソケットへwrite()する。
 */
interface EscPosPrinter {
    fun print(bytes: ByteArray)
}

class LoggingEscPosPrinter : EscPosPrinter {
    override fun print(bytes: ByteArray) {
        Log.i("EscPosPrinter", "ESC/POS bytes length=${bytes.size} (stub, not sent)")
    }
}

/**
 * Simple Bluetooth ESC/POS sender (SPP). Requires BLUETOOTH_CONNECT/SCAN permissions on Android 12+.
 * Usage: BluetoothEscPosPrinter(mac).print(bytes)
 */
class BluetoothEscPosPrinter(private val macAddress: String) : EscPosPrinter {
    override fun print(bytes: ByteArray) {
        try {
            val adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
            val device = adapter.getRemoteDevice(macAddress)
            val uuid = device.uuids?.firstOrNull()?.uuid ?: java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") // SPP
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            adapter.cancelDiscovery()
            socket.connect()
            socket.outputStream.use { it.write(bytes); it.flush() }
            socket.close()
            Log.i("EscPosPrinter", "Sent ESC/POS to Bluetooth $macAddress")
        } catch (e: Exception) {
            Log.e("EscPosPrinter", "Bluetooth print failed: ${e.message}", e)
        }
    }
}

/**
 * Simple TCP ESC/POS sender (LANプリンタ想定). host:port にバイト列をそのまま送る。
 */
class TcpEscPosPrinter(private val host: String, private val port: Int = 9100) : EscPosPrinter {
    override fun print(bytes: ByteArray) {
        try {
            java.net.Socket(host, port).use { socket ->
                socket.getOutputStream().use { it.write(bytes); it.flush() }
            }
            Log.i("EscPosPrinter", "Sent ESC/POS to $host:$port")
        } catch (e: Exception) {
            Log.e("EscPosPrinter", "TCP print failed: ${e.message}", e)
        }
    }
}
