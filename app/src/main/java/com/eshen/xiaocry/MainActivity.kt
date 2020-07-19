package com.eshen.xiaocry

import android.graphics.Rect
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eshen.xiaocry.adapter.JokeAdapter
import com.eshen.xiaocry.base.BaseActivity
import com.eshen.xiaocry.bean.JokeBean
import com.eshen.xiaocry.constant.APIConstants
import com.eshen.xiaocry.net.NetWorkUtils
import com.eshen.xiaocry.net.RequestCallback
import com.eshen.xiaocry.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : BaseActivity() {

    private var page = 0
    private var exitTime: Long = 0
    private var jokeList = ArrayList<JokeBean>()
    private lateinit var jokeAdapter: JokeAdapter
    private var showFAB: Boolean = false

    override fun initStatusBar() {
        StatusBarUtils.setStatusColor(
            this,
            isTranslate = false,
            isDarkText = true,
            bgColor = android.R.color.transparent
        )
    }

    override fun getContentViewResId(): Int = R.layout.activity_main

    override fun initActionBar() {
        supportActionBar?.hide()
    }

    override fun initView(rootView: View) {
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
                    parent: RecyclerView,
                    state: RecyclerView.State
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


    override fun initListener() {
        refresh_layout?.setOnRefreshListener {
            loadData(DATA_REFRESH)
        }
        jokeAdapter.setJokeListener(object : JokeAdapter.OnJokeListener {

            override fun onLoadMore() {
                loadData(DATA_LOAD_MORE)
            }

            override fun onItemLongClick(v: View) {
                val bean: JokeBean = v.tag as JokeBean
                ClipBoardUtils.copyClipBoard(this@MainActivity, "joke", bean.text)
                ToastUtils.showToast(
                    this@MainActivity,
                    getString(R.string.copy_success)
                )
            }
        })
        recycler_view?.addOnScrollListener(object : OnScrollListener() {
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
        floating_action_button?.setOnClickListener {
            recycler_view.scrollToPosition(0)
        }
    }

    override fun loadData(style: Int) {
        if (style == DATA_INIT) setUpState(PageStatus.LOADING)
        if (style == DATA_LOAD_MORE) {
            ++page
        } else {
            page = 0
        }
        val params = HashMap<String, String>()
        params["type"] = APIConstants.TYPE_TEXT
        params["count"] = APIConstants.DEFAULT_COUNT.toString()
        params["page"] = page.toString()
        val url = NetWorkUtils.makeUrl(APIConstants.BASE_URL, APIConstants.ACTION_GETJOKE, params)
        NetWorkUtils.doGet(url).doRequest(object : RequestCallback {

            override fun onResponseInUi(result: String) {
                jokeList = JSONParseUtils.parseJokeList(result)
                if (jokeList.size > 0) {
                    setUpState(PageStatus.SUCCESS)
                    jokeAdapter.setData(jokeList, page > 0)
                } else {
                    if (style == DATA_INIT) {
                        setUpState(PageStatus.EMPTY)
                    } else {
                        ToastUtils.showToast(
                            this@MainActivity,
                            getString(R.string.no_new_content)
                        )
                    }
                }
                refresh_layout.isRefreshing = false
            }

            override fun onFailureInUi(e: IOException) {
                if (style == DATA_INIT) {
                    setUpState(PageStatus.ERROR)
                } else {
                    ToastUtils.showToast(
                        this@MainActivity,
                        getString(R.string.check_network_status)
                    )
                }
                refresh_layout.isRefreshing = false
                e.printStackTrace()
            }
        })
    }

    override fun onClickRetry() {
        loadData(DATA_REFRESH)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(this, getString(R.string.press_exit_again))
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
