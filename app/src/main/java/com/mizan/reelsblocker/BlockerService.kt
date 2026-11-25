package com.mizan.reelsblocker
import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BlockerService : AccessibilityService() {
    private var last = 0L
    override fun onAccessibilityEvent(e: AccessibilityEvent) {
        val pl = Platforms.all.firstOrNull { it.pkg == e.packageName?.toString() } ?: return
        if (!Store.global(this) || Store.pauseLeftMs(this) > 0 || !Store.on(this, pl.id)) return
        val now = SystemClock.elapsedRealtime()
        if (now - last < 1000) return
        val root = rootInActiveWindow ?: return
        if (pl.keys.isEmpty() || hasId(root, pl.keys, 0)) {
            last = now
            Store.addBlock(this)
            performGlobalAction(GLOBAL_ACTION_BACK)
            startActivity(Intent(this, WarningActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }
    }
    private fun hasId(n: AccessibilityNodeInfo?, keys: List<String>, depth: Int): Boolean {
        if (n == null || depth > 25) return false
        val id = n.viewIdResourceName
        if (id != null && keys.any { id.contains(it, true) }) return true
        for (i in 0 until n.childCount) if (hasId(n.getChild(i), keys, depth + 1)) return true
        return false
    }
    override fun onInterrupt() {}
}
