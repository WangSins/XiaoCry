package com.eshen.xiaocry

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.KeyEvent
import android.view.View
import com.eshen.xiaocry.adapter.OnRVListener
import com.eshen.xiaocry.adapter.JokeRVAdapter
import com.eshen.xiaocry.bean.JokeBean
import com.eshen.xiaocry.constant.APIConstants
import com.eshen.xiaocry.net.NetWorkUtils
import com.eshen.xiaocry.net.RequestCallback
import com.eshen.xiaocry.util.DensityUtils
import com.eshen.xiaocry.util.JSONParseUtils
import com.eshen.xiaocry.util.StatusBarUtils
import com.eshen.xiaocry.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var page = 0
    private var exitTime: Long = 0
    private var jokeList = ArrayList<JokeBean>()
    private lateinit var jokeRVAdapter: JokeRVAdapter

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
        jokeRVAdapter.setRVListener(object : OnRVListener {

            override fun onLoadMore() {
                loadMore(false)
            }
        })
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        val layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        )
        recycler_view.layoutManager = layoutManager
        jokeRVAdapter = JokeRVAdapter()
        recycler_view.addItemDecoration(object : ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                super.getItemOffsets(outRect, view, parent, state)
                val space = DensityUtils.dip2px(view.context, 4f)
                outRect.let {
                    it.left = space
                    it.top = space
                    it.right = space
                    it.bottom = space
                }
            }
        })
        recycler_view.adapter = jokeRVAdapter
    }

    private fun loadMore(isRefresh: Boolean) = if (NetWorkUtils.isNetworkAvailable(this)) {
        jokeList.clear()
        if (isRefresh) {
            page = 0
        } else {
            ++page
        }
        val params = HashMap<String, String>()
        params["type"] = APIConstants.TYPE_TEXT.toString()
        params["page"] = page.toString()
        val url = NetWorkUtils.makeUrl(APIConstants.BASE_URL, APIConstants.ACTION_GETJOKE, params)
        NetWorkUtils.doGet(url).doRequest(object : RequestCallback {

            override fun requestSuccess(response: Response) {
                jokeList = JSONParseUtils.parseJokeList(response.body()?.string())
                runOnUiThread {
                    setData()
                }
            }

            override fun requestError(e: IOException) {
                e.printStackTrace()
            }
        })
    } else {
        ToastUtils.showToast(this, resources.getString(R.string.check_network_status))
        refresh_layout.isRefreshing = false
    }

    private fun setData() {
        jokeRVAdapter.setData(this.jokeList, page == 0)
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
