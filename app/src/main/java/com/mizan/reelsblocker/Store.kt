package com.mizan.reelsblocker
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

data class Platform(val id: String, val name: String, val feature: String, val pkg: String, val keys: List<String>)

object Platforms {
    val all = listOf(
        Platform("fb", "Facebook", "Facebook Reels", "com.facebook.katana", listOf("reel")),
        Platform("ig", "Instagram", "Instagram Reels", "com.instagram.android", listOf("clips_viewer")),
        Platform("yt", "YouTube", "YouTube Shorts", "com.google.android.youtube", listOf("reel_recycler", "reel_player_page")),
        Platform("tt", "TikTok", "TikTok Videos", "com.zhiliaoapp.musically", emptyList()),
        Platform("sc", "Snapchat", "Snapchat Spotlight", "com.snapchat.android", listOf("spotlight"))
    )
}

object Store {
    const val PAUSE_MS = 5 * 60 * 1000L
    private fun p(c: Context) = c.getSharedPreferences("mrb", 0)
    fun global(c: Context) = p(c).getBoolean("g", true)
    fun setGlobal(c: Context, v: Boolean) = p(c).edit().putBoolean("g", v).apply()
    fun on(c: Context, id: String) = p(c).getBoolean("p_$id", true)
    fun setOn(c: Context, id: String, v: Boolean) = p(c).edit().putBoolean("p_$id", v).apply()
    fun pauseLeftMs(c: Context) = (p(c).getLong("pu", 0) - System.currentTimeMillis()).coerceAtLeast(0)
    fun startPause(c: Context) { if (pauseLeftMs(c) == 0L) p(c).edit().putLong("pu", System.currentTimeMillis() + PAUSE_MS).apply() }
    private fun day() = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
    fun addBlock(c: Context) { val k = "b_${day()}"; p(c).edit().putInt(k, p(c).getInt(k, 0) + 1).apply() }
    fun blockedToday(c: Context) = p(c).getInt("b_${day()}", 0)
    fun activeRules(c: Context) = Platforms.all.count { on(c, it.id) }
}
