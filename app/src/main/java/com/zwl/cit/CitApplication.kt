package com.zwl.cit

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import com.alibaba.android.arouter.launcher.ARouter
import com.zwl.common.plugin.PluginManager
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
@HiltAndroidApp
class CitApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        PluginManager.init(this)
    }


    override fun onCreate() {
        Companion.context = this
        if (isDebug(this)) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        super.onCreate()

        for (pluginItem in PluginManager.plugins) {
            try {
                val clazz: Class<*> =
                    PluginManager.mNowClassLoader.loadClass(pluginItem.applicationName)
                val application = clazz.newInstance() as Application
                    ?: continue
                application.onCreate()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 判断当前应用是否是debug状态
     */
    fun isDebug(context: Context): Boolean {
        return try {
            val info: ApplicationInfo = context.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        public lateinit var context: Context
    }
}