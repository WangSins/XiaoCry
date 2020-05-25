package com.eshen.xiaocry.util

import com.eshen.xiaocry.bean.JokeBean
import org.json.JSONObject
import java.util.*

/**
 * Created by Sin on 2019/7/27
 */
object JSONParseUtils {

    /**
     * 解析段子列表
     */
    fun parseJokeList(response: String?): ArrayList<JokeBean> {
        val jokeList = ArrayList<JokeBean>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("result")
        for (i in 0 until jsonArray.length()) {
            val ob = jsonArray.get(i) as JSONObject
            val jokeBean = JokeBean()
            jokeBean.text = ob.getString("text")
            jokeList.add(jokeBean)
        }
        return jokeList
    }
}