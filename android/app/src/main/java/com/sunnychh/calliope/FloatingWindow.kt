package com.sunnychh.calliope

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.facebook.react.ReactApplication
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager


class FloatingWindowService(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return "FloatingWindowService"
    }

    private var windowManager: WindowManager? = null
    private var floatView: ReactRootView? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private val handler = Handler(Looper.getMainLooper())

    @ReactMethod
    fun showFloatWindow(width: Int, height: Int, left: Int, top: Int) {
        UiThreadUtil.runOnUiThread {
            if (!Settings.canDrawOverlays(reactApplicationContext)) {
                // 悬浮窗权限未授予
                return@runOnUiThread
            }

            if (floatView != null) {
                // 悬浮窗已经显示
                return@runOnUiThread
            }

            // 初始化 WindowManager
            windowManager = reactApplicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            // 创建 ReactRootView 来加载 React Native 组件
            floatView = ReactRootView(reactApplicationContext).apply {
                val application = reactApplicationContext.applicationContext as ReactApplication
                val reactInstanceManager: ReactInstanceManager = application.reactNativeHost.reactInstanceManager

                val props = Bundle().apply {
                    putIntArray("size", intArrayOf(width, height))
                    putIntArray("initPosition", intArrayOf(left, top))
                }

                startReactApplication(reactInstanceManager, "FloatWindowComponent", props) // "FloatWindowComponent" 是你在 React Native 中注册的组件
            }

            // 悬浮窗的布局参数
            layoutParams = WindowManager.LayoutParams(
                dpToPx(reactApplicationContext, width),
                dpToPx(reactApplicationContext, height),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.LEFT
                x = dpToPx(reactApplicationContext, left)
                y = dpToPx(reactApplicationContext, top)
            }

            // 将悬浮窗添加到 WindowManager
            windowManager?.addView(floatView, layoutParams)
        }
    }

    @ReactMethod
    fun hideFloatWindow() {
        UiThreadUtil.runOnUiThread {
            if (floatView != null) {
                windowManager?.removeView(floatView)
                floatView = null
            }
        }
    }

    @ReactMethod
    fun moveFloatWindow(left: Int, top: Int, moveDone: Callback) {
        handler.post {
            if (floatView != null && layoutParams != null) {
                layoutParams!!.x = dpToPx(reactApplicationContext, left)
                layoutParams!!.y = dpToPx(reactApplicationContext, top)
                windowManager?.updateViewLayout(floatView, layoutParams)
            }
            moveDone.invoke(left, top)
        }
    }
}

class FloatingWindowPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(FloatingWindowService(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<View, ReactShadowNode<*>>> {
        return emptyList()
    }
}
