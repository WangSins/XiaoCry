package com.eshen.xiaocry

import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eshen.xiaocry.adapter.JokeAdapter
import com.eshen.xiaocry.bean.JokeBean
import com.eshen.xiaocry.constant.APIConstants
import com.eshen.xiaocry.net.NetWorkUtils
import com.eshen.xiaocry.net.RequestCallback
import com.eshen.xiaocry.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var page = 0
    private var exitTime: Long = 0
    private var jokeList = ArrayList<JokeBean>()
    private lateinit var jokeAdapter: JokeAdapter
    private var showFAB: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        initActionBar()
        initView()
        loadMore(true)
        initListener()
    }

    private fun initActionBar() {
        supportActionBar?.hide()
    }

    private fun initStatusBar() {
        StatusBarUtils.setStatusColor(
            this,
            isTranslate = false,
            isDarkText = true,
            bgColor = android.R.color.transparent
        )
    }

    private fun initListener() {
        refresh_layout.setOnRefreshListener {
            loadMore(true)
        }
        jokeAdapter.setJokeListener(object : JokeAdapter.OnJokeListener {

            override fun onLoadMore() {
                loadMore(false)
            }
        })
        recycler_view.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.canScrollVertically(-1)) {
                    //能向上滚动，离开顶部
                    if (!showFAB) {
                        AnimatorUtil.scaleObjectAnimation(floating_action_button, 0f, 1f, 400)
                        showFAB = true
                    }
                } else {
                    //不能向上滚动，到达顶部
                    if (showFAB) {
                        AnimatorUtil.scaleObjectAnimation(floating_action_button, 1f, 0f, 300)
                        showFAB = false
                    }
                }
            }
        })
        floating_action_button.setOnClickListener {
            recycler_view.scrollToPosition(0)
        }
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        jokeAdapter = JokeAdapter()
        recycler_view.run {
            layoutManager = staggeredGridLayoutManager
            adapter = jokeAdapter
            addItemDecoration(object : ItemDecoration() {

                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: androidx.recyclerview.widget.RecyclerView,
                    state: State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.let {
                        it.left = 4.dp
                        it.top = 4.dp
                        it.right = 4.dp
                        it.bottom = 4.dp
                    }
                }
            })
        }
    }

    private fun loadMore(isRefresh: Boolean) = if (NetWorkUtils.isNetworkAvailable(this)) {
        jokeList.clear()
        if (isRefresh) {
            page = 0
        } else {
            ++page
        }
        val params = HashMap<String, String>()
        params["type"] = APIConstants.TYPE_TEXT
        params["count"] = APIConstants.DEFAULT_COUNT.toString()
        params["page"] = page.toString()
        val url = NetWorkUtils.makeUrl(APIConstants.BASE_URL, APIConstants.ACTION_GETJOKE, params)
        NetWorkUtils.doGet(url).doRequest(object : RequestCallback {

            override fun onResponseInUi(result: String) {
                jokeList = JSONParseUtils.parseJokeList(result)
                jokeAdapter.setData(jokeList, page > 0)
                refresh_layout.isRefreshing = false
            }

            override fun onFailureInUi(e: IOException) {
                refresh_layout.isRefreshing = false
                e.printStackTrace()
            }
        })
    } else {
        ToastUtils.showToast(this, resources.getString(R.string.check_network_status))
        refresh_layout.isRefreshing = false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(this, resources.getString(R.string.press_exit_again))
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
