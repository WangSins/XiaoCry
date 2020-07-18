package com.eshen.xiaocry.net

import java.io.IOException

/**
 * Created by Sin on 2019/7/27
 */
interface RequestCallback {

    /**
     * 请求成功回调
     */
    fun onResponseInUi(result: String)

    /**
     * 请求失败回调
     */
    fun onFailureInUi(e: IOException)

}