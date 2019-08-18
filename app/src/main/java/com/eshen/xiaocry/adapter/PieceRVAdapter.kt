package com.eshen.xiaocry.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eshen.xiaocry.R
import com.eshen.xiaocry.bean.PieceBean
import com.eshen.xiaocry.util.ClipBoardUtils
import com.eshen.xiaocry.util.ToastUtils

/**
 * Created by Sin on 2019/7/27
 */
class PieceRVAdapter : RecyclerView.Adapter<PieceRVAdapter.PieceHolder>() {

    private var pieceList = ArrayList<PieceBean>()
    private lateinit var onRVListener: OnRVListener

    fun setData(list: ArrayList<PieceBean>, isRefresh: Boolean) {
        if (isRefresh) {
            this.pieceList.clear()
        }
        this.pieceList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PieceHolder {
        val mItemView: View = LayoutInflater.from(p0.context).inflate(R.layout.item_piece, null)
        return PieceHolder(mItemView)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = pieceList.size

    override fun onBindViewHolder(p0: PieceHolder, p1: Int) {
        p0.pieceText.text = pieceList[p1].text
        p0.itemView.setOnLongClickListener {
            ClipBoardUtils.copyClipBoard(it.context, "piece", pieceList[p1].text)
            ToastUtils.showToast(it.context, it.context.resources.getString(R.string.copy_success))
            true
        }
        if (p1 == itemCount - 1) {
            onRVListener.onLoadMore()
        }
    }

    class PieceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pieceText: TextView = itemView.findViewById(R.id.piece_text)
    }

    fun setRVListener(onRVListener: OnRVListener) {
        this.onRVListener = onRVListener
    }
}