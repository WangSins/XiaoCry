package com.eshen.xiaocry.net

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder


/**
 * Created by Sin on 2019/7/27
 */
object NetWorkUtils {

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }
    private lateinit var requestCallback: RequestCallback

    fun doGet(url: String): NetWorkUtils {
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) {
                        throw  IOException("error code: " + response.code());
                    }
                    response.body()?.string()?.let {
                        handler.post {
                            requestCallback.onResponseInUi(it)
                        }
                    }
                } catch (e: IOException) {
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    requestCallback.onFailureInUi(e)
                }
            }

        })
        return this
    }

    fun doRequest(requestCallback: RequestCallback) {
        this.requestCallback = requestCallback
    }

    fun makeUrl(baseUrl: String, action: String, params: Map<String, String>?): String {
        var url = baseUrl + action
        if (params == null || params.isEmpty()) {
            return url
        }

        var i = 0
        for ((key, value) in params) {
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                continue
            }

            url += if (i == 0) "?" else "&"
            url += key + "=" + URLEncoder.encode(value)
            i++
        }
        return url
    }
}