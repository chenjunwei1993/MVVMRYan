package com.android.mvvmryan.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.android.mvvmryan.arouter.ARouterAddress
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * Created by goldze on 2017/6/15.
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<*>> : RxFragment(), IBaseView {
    private var binding: V? = null
    private var viewModel: VM? = null
    private var viewModelId: Int = 0

    val isBackPressed: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            viewModel = createViewModel(this@BaseFragment, BaseViewModel::class.java) as VM
        }
        binding?.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)
        //注入RxLifecycle生命周期
        viewModel?.injectLifecycleProvider(this)
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun registerUIChangeLiveDataCallBack() {
        viewModel?.run {
            //跳入新页面
            uc.startActivityEvent.observe(this@BaseFragment, Observer<Map<String, Any?>> { params ->
                params?.let {
                    val path = it[BaseViewModel.ParameterField.PATH] as String?
                    val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                    this@BaseFragment.startActivity(path, bundle)
                }
            })
            //跳入ContainerActivity
            uc.startContainerActivityEvent.observe(this@BaseFragment, Observer<Map<String, Any?>> { params ->
                params?.let {
                    val canonicalPath = it[BaseViewModel.ParameterField.CANONICAL_PATH] as String?
                    val bundle = it[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                    this@BaseFragment.startContainerActivity(canonicalPath, bundle)
                }
            })
            //关闭界面
            uc.finishEvent.observe(this@BaseFragment, Observer { activity?.finish() })
            //关闭上一层
            uc.onBackPressedEvent.observe(this@BaseFragment, Observer { activity?.onBackPressed() })
        }
    }


    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(path: String?, bundle: Bundle? = Bundle()) {
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
    abstract fun initContentView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): Int

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
    private fun initViewModel(): VM? {
        return null
    }

    override fun initData() {

    }

    override fun initViewObservable() {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    private fun <T : ViewModel> createViewModel(fragment: Fragment, cls: Class<T>): T {
        return ViewModelProviders.of(fragment).get(cls)
    }
}
