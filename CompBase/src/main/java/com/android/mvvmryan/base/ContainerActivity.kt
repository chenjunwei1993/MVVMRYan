package com.android.mvvmryan.base

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.android.mvvmryan.R
import com.android.mvvmryan.arouter.ARouterAddress
import com.android.mvvmryan.arouter.ARouterInjectable
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import java.lang.ref.WeakReference


/**
 *@author chenjunwei
 *@desc 盛装Fragment的一个容器(代理)Activity 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 *@date 2019-10-28
 */
@Route(path = ARouterAddress.CONTAINER)
class ContainerActivity : RxAppCompatActivity(), ARouterInjectable {

    companion object {
        const val FRAGMENT_TAG = "content_fragment_tag"
    }

    private lateinit var mFragment: WeakReference<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        var fragment: Fragment? = null
        if (savedInstanceState != null) {
            fragment = supportFragmentManager.getFragment(savedInstanceState, FRAGMENT_TAG)
        }
        if (fragment == null) {
            fragment = getFragment()
        }
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.content, fragment)
        trans.commitAllowingStateLoss()
        mFragment = WeakReference(fragment)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, FRAGMENT_TAG, mFragment.get()!!)
    }

    private fun getFragment(): Fragment {
        val canonicalPath = intent.getStringExtra(BaseViewModel.ParameterField.CANONICAL_PATH)
        return ARouter.getInstance().build(canonicalPath).navigation() as Fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment is BaseFragment<*, *>) {
            if (!fragment.isBackPressed) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}