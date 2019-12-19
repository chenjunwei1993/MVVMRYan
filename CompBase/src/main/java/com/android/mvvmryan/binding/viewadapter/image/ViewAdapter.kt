package com.android.mvvmryan.binding.viewadapter.image


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android.library.CoreImageLoaderUtil

/**
 *@author chenjunwei
 *@desc
 *@date 2019-12-03
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["url", "placeholderRes", "failResId", "isGif", "width", "height"], requireAll = false)
    fun setImageUri(imageView: ImageView, url: Any, placeholderRes: Any?, failResId: Any?, isGif: Boolean, width: Int, height: Int) {
        if (isGif) {
            //使用Glide框架加载图片
            CoreImageLoaderUtil.loadingGifImg(imageView.context, url, imageView, width, height, placeholderRes, failResId, null)
        } else {
            //使用Glide框架加载图片
            CoreImageLoaderUtil.loadingImg(imageView.context, url, imageView, placeholderRes, failResId, null, width, height)
        }

    }
}

