package com.eshen.xiaocry.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.eshen.xiaocry.R
import com.eshen.xiaocry.base.BaseActivity.PageStatus.*

/**
 * Created by Sin on 2020/7/19
 */
abstract class BaseActivity : AppCompatActivity() {

    private var currentState = NONE
    private var successView: View? = null
    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null

    companion object {
        const val DATA_INIT = 0
        const val DATA_REFRESH = 1
        const val DATA_LOAD_MORE = 2
    }

    enum class PageStatus {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private var baseContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        val rootView: View = loadRootView(layoutInflater, null)
        setContentView(rootView)
        initActionBar()
        baseContainer = rootView.findViewById(R.id.base_container)
        loadStateView(layoutInflater, baseContainer as ViewGroup)
        initView(rootView)
        loadData(DATA_INIT)
        initListener()
    }

    open fun initStatusBar() {}
    abstract fun getContentViewResId(): Int
    open fun initActionBar() {}
    open fun initView(rootView: View) {}
    open fun initListener() {}
    open fun loadData(style: Int) {}

    open fun loadStateView(inflater: LayoutInflater, container: ViewGroup) {
        container.run {
            successView = loadSuccessView(inflater, this)
            addView(successView)
            loadingView = loadLoadingView(inflater, this)
            addView(loadingView)
            errorView = loadErrorView(inflater, this)
            addView(errorView)
            emptyView = loadEmptyView(inflater, this)
            addView(emptyView)
            setUpState(NONE)
        }
    }

    fun setUpState(currentPageStatus: PageStatus) {
        this.currentState = currentPageStatus
        successView?.visibility = if (currentPageStatus == SUCCESS) View.VISIBLE else View.GONE
        loadingView?.visibility = if (currentPageStatus == LOADING) View.VISIBLE else View.GONE
        errorView?.visibility = if (currentPageStatus == ERROR) View.VISIBLE else View.GONE
        emptyView?.visibility = if (currentPageStatus == EMPTY) View.VISIBLE else View.GONE
    }

    open fun loadLoadingView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        return inflater.inflate(R.layout.layout_loading, container, false)
    }


    open fun loadErrorView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        return inflater.inflate(R.layout.layout_error, container, false)
    }

    open fun loadEmptyView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        return inflater.inflate(R.layout.layout_empty, container, false)
    }

    open fun loadSuccessView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        return inflater.inflate(getContentViewResId(), container, false)
    }

    open fun loadRootView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return inflater.inflate(R.layout.activity_base_layout, container, false)
    }

    fun onRetry(view: View) {
        this.onClickRetry()
    }

    open fun onClickRetry() {}
}