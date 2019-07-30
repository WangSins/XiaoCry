package com.eshen.xiaocry.net

import okhttp3.Response
import java.io.IOException

/**
 * Created by Sin on 2019/7/27
 */
interface RequestCallback {

    /**
     * 请求成功回调
     */
    fun requestSuccess(response: Response)

    /**
     * 请求失败回调
     */
    fun requestError(e: IOException)

}