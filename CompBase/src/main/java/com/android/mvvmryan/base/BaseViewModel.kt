package com.android.mvvmryan.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.android.mvvmryan.bus.SingleLiveEvent
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference
import java.util.*

/**
 *@author chenjunwei
 *@desc
 *@date 2019-12-04
 */
open class BaseViewModel<M : BaseModel> @JvmOverloads constructor(application: Application, private var model: M? = null) : AndroidViewModel(application), IBaseViewModel, Consumer<Disposable> {
    var uc: UIChangeLiveData = UIChangeLiveData()
    //弱引用持有
    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable? = null

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    private fun addSubscribe(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    /**
     * 跳转页面
     *
     * @param path   Arouter Activity对应path
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(path: String?, bundle: Bundle? = null) {
        val params = HashMap<String, Any?>()
        params[ParameterField.PATH] = path
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uc.startActivityEvent.postValue(params)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalPath Arouter Fragment对应path
     * @param bundle        跳转所携带的信息
     */
    @JvmOverloads
    fun startContainerActivity(canonicalPath: String?, bundle: Bundle? = null) {
        val params = HashMap<String, Any?>()
        params[ParameterField.CANONICAL_PATH] = canonicalPath
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uc.startContainerActivityEvent.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        uc.finishEvent.call()
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        uc.onBackPressedEvent.call()
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {}

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun onStart() {}

    override fun onStop() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onCleared() {
        super.onCleared()
        model?.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
        mCompositeDisposable?.clear()
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable) {
        addSubscribe(disposable)
    }

    inner class UIChangeLiveData : SingleLiveEvent<Any>() {
        var startActivityEvent: SingleLiveEvent<Map<String, Any?>> = SingleLiveEvent()
        var startContainerActivityEvent: SingleLiveEvent<Map<String, Any?>> = SingleLiveEvent()
        var finishEvent: SingleLiveEvent<Void?> = SingleLiveEvent()
        var onBackPressedEvent: SingleLiveEvent<Void?> = SingleLiveEvent()
    }

    object ParameterField {
        var PATH = "PATH"
        var CANONICAL_PATH = "CANONICAL_PATH"
        var BUNDLE = "BUNDLE"
    }
}
