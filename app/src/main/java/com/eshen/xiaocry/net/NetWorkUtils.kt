package com.eshen.xiaocry.net

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.*
import java.io.IOException

/**
 * Created by Sin on 2019/7/27
 */
object NetWorkUtils {

    private lateinit var requestCallback: RequestCallback

    /**
     * 获取文字段子
     */
    fun doGetPiece(url: String): NetWorkUtils {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestCallback.requestError(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                requestCallback.requestSuccess(response)
            }
        })
        return this
    }

    fun doRequest(requestCallback: RequestCallback) {
        this.requestCallback = requestCallback
    }

    fun isNetworkAvailable(context: Context): Boolean {
        var hasWifiCon = false
        var hasMobileCon = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfoList = cm.allNetworkInfo
        for (net in netInfoList) {

            val type = net.typeName
            if (type.equals("WIFI", ignoreCase = true)) {
                if (net.isConnected) {
                    hasWifiCon = true
                }
            }

            if (type.equals("MOBILE", ignoreCase = true)) {
                if (net.isConnected) {
                    hasMobileCon = true
                }
            }
        }
        return hasWifiCon || hasMobileCon
    }
}