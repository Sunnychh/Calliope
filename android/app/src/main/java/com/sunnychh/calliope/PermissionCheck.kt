package com.sunnychh.calliope

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.app.ActivityCompat
import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

/**
 * PermissionCheck
 *
 * 检查并获取应用程序需要的额外动态权限
 */
class PermissionCheck(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return "PermissionCheck"
    }

    @ReactMethod
    fun checkOverlayPermission(callback: Callback) {
        if (Settings.canDrawOverlays(reactApplicationContext)) {
            callback.invoke(true)
        } else {
            callback.invoke(false)
        }
    }

    @ReactMethod
    fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + reactApplicationContext.packageName)
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ActivityCompat.startActivity(reactApplicationContext, intent, null)
    }

    @ReactMethod
    fun checkAccessibilityPermission(callback: Callback) {
        val am = reactApplicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (am.isEnabled && am.isTouchExplorationEnabled) {
            callback.invoke(true)
        } else {
            callback.invoke(false)
        }
    }

    @ReactMethod
    fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ActivityCompat.startActivity(reactApplicationContext, intent, null)
    }
}

class PermissionPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(PermissionCheck(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<View, ReactShadowNode<*>>> {
        return emptyList()
    }
}
