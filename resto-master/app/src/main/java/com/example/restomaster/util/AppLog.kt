package com.example.restomaster.util

import android.util.Log

/**
 * 薄いロガー。タグを限定し、後でLogcatフィルタしやすくする。
 */
object AppLog {
    private const val TAG_HTTP = "HTTP"
    private const val TAG_DB = "DB"
    private const val TAG_SERVER = "SERVER"

    fun http(msg: String) {
        Log.i(TAG_HTTP, msg)
    }

    fun db(msg: String) {
        Log.i(TAG_DB, msg)
    }

    fun server(msg: String) {
        Log.i(TAG_SERVER, msg)
    }

    fun error(tag: String, msg: String, t: Throwable? = null) {
        Log.e(tag, msg, t)
    }
}
