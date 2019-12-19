package com.android.mvvmryan.binding.viewadapter.webview

import android.text.TextUtils
import android.webkit.WebView
import androidx.databinding.BindingAdapter

/**
 *@author chenjunwei
 *@desc WebView
 *@date 2019-10-30
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter("render")
    fun loadHtml(webView: WebView, html: String) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        }
    }
}
