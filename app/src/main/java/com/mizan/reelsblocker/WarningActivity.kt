package com.mizan.reelsblocker
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class WarningActivity : ComponentActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContent {
            Column(
                Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF1A0B3B), Color(0xFF5B2BD6))))
                    .padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally
            ) {
                Text("🚫", fontSize = 80.sp)
                Spacer(Modifier.height(16.dp))
                Text("Oops! Reels are blocked right now!", color = Color.White, fontSize = 26.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                Text("Don't waste your valuable time scrolling. Stay focused and make your time count.",
                    color = Color(0xFFD9C8FF), textAlign = TextAlign.Center)
                Spacer(Modifier.height(32.dp))
                Button({ Store.startPause(this@WarningActivity); finish() }, Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(Color(0xFFFFC107), Color.Black)) { Text("Pause for 5 Minutes") }
                Spacer(Modifier.height(12.dp))
                Button({
                    startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()
                }, Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF8E5CFF))) { Text("Go Back") }
            }
        }
    }
}
