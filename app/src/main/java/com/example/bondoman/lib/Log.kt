package com.example.bondoman.lib

import android.util.Log
import com.github.mikephil.charting.BuildConfig

object Log {
    const val LOG = BuildConfig.DEBUG
    fun i(tag: String?, string: String?) {
        if (LOG) Log.i(tag, string!!)
    }

    fun e(tag: String?, string: String?) {
        if (LOG) Log.e(tag, string!!)
    }

    fun e(tag: String?, string: String?, e: Exception) {
        if (LOG) Log.e(tag, string!!, e)
    }

    fun d(tag: String?, string: String?) {
        if (LOG) Log.d(tag, string!!)
    }

    fun v(tag: String?, string: String?) {
        if (LOG) Log.v(tag, string!!)
    }

    fun w(tag: String?, string: String?) {
        if (LOG) Log.w(tag, string!!)
    }
}