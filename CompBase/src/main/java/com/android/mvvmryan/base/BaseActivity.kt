package com.android.mvvmryan.base

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.android.mvvmryan.arouter.ARouterAddress
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity


/**
 *@author chenjunwei
 *一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 *@date 2019-12-04
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>> : RxAppCompatActivity(), IBaseView {
    protected var binding: V? = null
    private var viewModel: VM? = null
    private var viewModelId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接受的参数方法
        initParam()
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }

    /**
     * 注入绑定
     *
     * @param savedInstanceState
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this@BaseActivity, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            viewModel = createViewModel(this@BaseActivity, BaseViewModel::class.java) as VM
        }
        //关联ViewModel
        binding?.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)
        //注入RxLifecycle生命周期
        viewModel?.injectLifecycleProvider(this@BaseActivity)
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun registerUIChangeLiveDataCallBack() {
        viewModel?.run {
            //跳入新页面
            uc.startActivityEvent.observe(this@BaseActivity, Observer<Map<String, Any?>> { params ->
                params?.let {
                    val path = it[BaseViewModel.ParameterField.PATH] as String?
                    val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                    this@BaseActivity.startActivity(path, bundle)
                }
            })
            //跳入ContainerActivity
            uc.startContainerActivityEvent.observe(this@BaseActivity, Observer<Map<String, Any?>> { params ->
                params?.let {
                    val canonicalPath = it[BaseViewModel.ParameterField.CANONICAL_PATH] as String?
                    val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                    this@BaseActivity.startContainerActivity(canonicalPath, bundle)
                }
            })
            //关闭界面
            uc.finishEvent.observe(this@BaseActivity, Observer { finish() })
            //关闭上一层
            uc.onBackPressedEvent.observe(this@BaseActivity, Observer { onBackPressed() })
        }
    }


    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(path: String?, bundle: Bundle? = null) {
        ARouter.getInstance().build(path).with(bundle).navigation()
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalPath Arouter Fragment路径
     * @param bundle        跳转所携带的信息
     */
    fun startContainerActivity(canonicalPath: String?, bundle: Bundle?) {
        bundle?.putString(BaseViewModel.ParameterField.CANONICAL_PATH,canonicalPath)
        ARouter.getInstance().build(ARouterAddress.CONTAINER).with(bundle).navigation()
    }

    /**
     * 刷新布局
     */
    fun refreshLayout() {
        if (viewModel != null) {
            binding?.setVariable(viewModelId, viewModel)
        }
    }

    override fun initParam() {

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(@Nullable savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    fun initViewModel(): VM? {
        return null
    }

    override fun initData() {

    }

    override fun initViewObservable() {

    }

    /**
     * 创建ViewModel
     *
     * @param activity
     * @param <T>
     * @return
    </T> */
    private fun <T : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
        return ViewModelProviders.of(activity).get(cls)
    }
}
