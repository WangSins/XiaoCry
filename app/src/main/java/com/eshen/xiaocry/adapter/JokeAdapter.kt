package com.eshen.xiaocry.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eshen.xiaocry.R
import com.eshen.xiaocry.bean.JokeBean

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
        p0.jokeContentTv.text = jokeList[p1].text
        p0.itemView.setOnLongClickListener {
            it.tag = jokeList[p1]
            onJokeListener.onItemLongClick(it)
            true
        }
        if (p1 == itemCount - 1) {
            onJokeListener.onLoadMore()
        }
    }

    class JokeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jokeContentTv: TextView = itemView.findViewById(R.id.joke_content_tv)
    }

    interface OnJokeListener {
        fun onLoadMore()
        fun onItemLongClick(v: View)
    }

    fun setJokeListener(onJokeListener: OnJokeListener) {
        this.onJokeListener = onJokeListener
    }
}