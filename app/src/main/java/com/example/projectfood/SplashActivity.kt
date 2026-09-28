package com.example.projectfood

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 食パンをゆらゆら揺らすアニメーションを開始
        val bread = findViewById<TextView>(R.id.tvBread)
        bread.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

        // アニメーションが終わる頃（2.2秒後）にメイン画面へ遷移
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // スプラッシュ画面はバックスタックに残さない
        }, 2200)
    }
}