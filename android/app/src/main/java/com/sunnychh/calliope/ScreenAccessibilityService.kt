package com.sunnychh.calliope

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ScreenAccessibilityService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var windowManager: WindowManager
    private var layoutParams: WindowManager.LayoutParams? = null
    private var floatingView: View? = null

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        showFloatingWindow()
    }

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent) {
        // 可以根据需要处理无障碍事件
    }

    override fun onInterrupt() {
        Log.i("com.sunnychh.calliope.ScreenAccessibilityService", "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i("com.sunnychh.calliope.ScreenAccessibilityService", "onServiceConnected")
    }

    private fun showFloatingWindow() {
        // 设置悬浮窗的布局参数
        layoutParams = WindowManager.LayoutParams(
            400,
            400,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        layoutParams!!.gravity = Gravity.TOP or Gravity.START
        layoutParams!!.x = 0
        layoutParams!!.y = 0

        // 创建一个悬浮窗布局
        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundColor(0xAAFFEB3B.toInt()) // 设置背景颜色
        }

        val titleTextView = TextView(this).apply {
            text = "悬浮窗标题"
            textSize = 18f
        }

        val button = Button(this).apply {
            text = "按钮"
            setOnClickListener {
                onButtonClick()
            }
        }

        val closeButton = Button(this).apply {
            text = "关闭"
            setOnClickListener {
                onCloseClick()
            }
        }

        // 将 TextView 和 Button 添加到 LinearLayout 中
        linearLayout.addView(titleTextView)
        linearLayout.addView(button)
        linearLayout.addView(closeButton)

        // 监听触摸事件，实现拖动悬浮窗
        linearLayout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 记录初始位置
                    initialX = layoutParams!!.x
                    initialY = layoutParams!!.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // 更新悬浮窗的位置
                    layoutParams!!.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams!!.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(floatingView, layoutParams)
                    true
                }
                else -> false
            }
        }

        // 将 LinearLayout 作为悬浮窗视图
        floatingView = linearLayout

        // 将悬浮窗视图添加到 WindowManager
        windowManager.addView(floatingView, layoutParams)
    }

    private fun onButtonClick() {
        // 悬浮窗按钮点击事件
        handler.post {
            Toast.makeText(this, "按钮被点击了", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onCloseClick() {
        // 移除悬浮窗
        floatingView?.let {
            windowManager.removeView(it)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let {
            windowManager.removeView(it)
        }
    }
}