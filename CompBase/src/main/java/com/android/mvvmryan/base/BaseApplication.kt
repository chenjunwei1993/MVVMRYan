package com.android.mvvmryan.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.android.library.util.CoreUtils
import com.android.mvvmryan.BuildConfig
import com.android.mvvmryan.arouter.ARouterInjectable

open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this@BaseApplication)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    @Synchronized
    fun setApplication(application: Application){
        CoreUtils.init(application)
        initARouter()
        sInstance = application
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                AppManager.removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                AppManager.addActivity(activity)
                handleActivity(activity)
            }

        })
    }

    /**
     * 阿里路由 接收参数
     */
    private fun handleActivity(activity: Activity?) {
        if (activity is ARouterInjectable) {
            ///注入ARouter参数
            ARouter.getInstance().inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            super.onFragmentCreated(fm, f, savedInstanceState)
                            if (f is ARouterInjectable) {
                                ///注入ARouter参数
                                ARouter.getInstance().inject(f)
                            }
                        }
                    }, true
            )
        }
    }

    /**
     * 阿里路由
     */
    private fun initARouter(){
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this@BaseApplication)
    }


    companion object{
        private var sInstance: Application? = null
    }
}