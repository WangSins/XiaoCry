package com.eshen.xiaocry.util

import com.eshen.xiaocry.bean.PieceBean
import org.json.JSONObject
import java.util.*

/**
 * Created by Sin on 2019/7/27
 */
object JSONParseUtils {

    /**
     * 解析段子列表
     */
    fun parsePieceList(response: String?): ArrayList<PieceBean> {
        val pieceList = ArrayList<PieceBean>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val ob = jsonArray.get(i) as JSONObject
            val pieceBean = PieceBean()
            pieceBean.text = ob.getString("text")
            pieceList.add(pieceBean)
        }
        return pieceList
    }
}