package com.mizan.reelsblocker
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private fun a11yOn() = (Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: "").contains(packageName)
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContent {
            var g by remember { mutableStateOf(Store.global(this)) }
            var tick by remember { mutableStateOf(0) }
            LaunchedEffect(Unit) { while (true) { delay(1000); tick++ } }
            val left = remember(tick) { Store.pauseLeftMs(this) / 1000 }
            val blocked = remember(tick) { Store.blockedToday(this) }
            val rules = remember(tick) { Store.activeRules(this) }
            val a11y = remember(tick) { a11yOn() }
            val P = Color(0xFF7A3FF2)
            LazyColumn(Modifier.fillMaxSize().background(Color(0xFF140A2E)).statusBarsPadding().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text("Mizan's Reels Blocker", color = Color.White, fontSize = 24.sp)
                    Text("Block Reels. Save Time. Stay Focused.", color = Color(0xFFD9C8FF))
                }
                item {
                    Card(colors = CardDefaults.cardColors(P), shape = RoundedCornerShape(24.dp)) {
                        Column(Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(if (g && left == 0L) "Reels Blocking: ON" else if (left > 0) "Paused" else "Reels Blocking: OFF", color = Color.White, fontSize = 20.sp)
                                    Text("$rules active rules · $blocked blocked today · ~${blocked * 3} min saved", color = Color(0xFFEADFFF))
                                }
                                Switch(g, { g = it; Store.setGlobal(this@MainActivity, it) })
                            }
                            if (left > 0) Text("Resumes in %02d:%02d".format(left / 60, left % 60), color = Color(0xFFFFC107), fontSize = 28.sp)
                        }
                    }
                }
                if (!a11y) item {
                    Button({ startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }, Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFFFFC107), Color.Black)) { Text("Enable Accessibility Service (required)") }
                }
                items(Platforms.all) { pl ->
                    var on by remember { mutableStateOf(Store.on(this@MainActivity, pl.id)) }
                    Card(colors = CardDefaults.cardColors(Color(0xFF2A1659)), shape = RoundedCornerShape(20.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) { Text(pl.feature, color = Color.White); Text(pl.name, color = Color(0xFFB9A3F0), fontSize = 12.sp) }
                            Switch(on, { on = it; Store.setOn(this@MainActivity, pl.id, it) })
                        }
                    }
                }
            }
        }
    }
}
