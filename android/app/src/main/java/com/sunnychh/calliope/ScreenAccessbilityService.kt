package com.sunnychh.calliope

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ScreenAccessbilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.i("ScreenAccessbilityService", "onAccessibilityEvent")
    }

    override fun onInterrupt() {
        Log.i("ScreenAccessbilityService", "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i("ScreenAccessbilityService", "onServiceConnected")
    }
}