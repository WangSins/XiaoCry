package com.eshen.xiaocry.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eshen.xiaocry.R
import com.eshen.xiaocry.bean.JokeBean
import com.eshen.xiaocry.util.ClipBoardUtils
import com.eshen.xiaocry.util.ToastUtils

/**
 * Created by Sin on 2019/7/27
 */
class JokeAdapter : RecyclerView.Adapter<JokeAdapter.JokeHolder>() {

    private var jokeList = ArrayList<JokeBean>()
    private lateinit var onJokeListener: OnJokeListener

    fun setData(list: ArrayList<JokeBean>, isAppend: Boolean) {
        if (!isAppend) {
            this.jokeList.clear()
        }
        this.jokeList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeHolder {
        val mItemView: View = LayoutInflater.from(p0.context).inflate(R.layout.item_joke, p0, false)
        return JokeHolder(mItemView)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = jokeList.size

    override fun onBindViewHolder(p0: JokeHolder, p1: Int) {
        p0.jokeText.text = jokeList[p1].text
        p0.itemView.setOnLongClickListener {
            ClipBoardUtils.copyClipBoard(it.context, "joke", jokeList[p1].text)
            ToastUtils.showToast(it.context, it.context.resources.getString(R.string.copy_success))
            true
        }
        if (p1 == itemCount - 1) {
            onJokeListener.onLoadMore()
        }
    }

    class JokeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jokeText: TextView = itemView.findViewById(R.id.joke_text)
    }

    interface OnJokeListener {
        fun onLoadMore()
    }

    fun setJokeListener(onJokeListener: OnJokeListener) {
        this.onJokeListener = onJokeListener
    }
}