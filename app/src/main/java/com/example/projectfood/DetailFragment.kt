package com.example.projectfood

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ResultFragmentから渡された店舗情報を受け取る
        val shopName = arguments?.getString("shopName") ?: ""
        val shopAddress = arguments?.getString("shopAddress") ?: ""
        val shopOpen = arguments?.getString("shopOpen") ?: ""
        val shopImage = arguments?.getString("shopImage") ?: ""
        val shopUrl = arguments?.getString("shopUrl") ?: ""
        val shopLat = arguments?.getDouble("shopLat") ?: 0.0
        val shopLng = arguments?.getDouble("shopLng") ?: 0.0

        val ivShopImage = view.findViewById<ImageView>(R.id.ivShopImage)
        val tvShopName = view.findViewById<TextView>(R.id.tvShopName)
        val tvShopAddress = view.findViewById<TextView>(R.id.tvShopAddress)
        val tvShopOpen = view.findViewById<TextView>(R.id.tvShopOpen)
        val btnOpenUrl = view.findViewById<Button>(R.id.btnOpenUrl)
        val btnOpenMap = view.findViewById<Button>(R.id.btnOpenMap)

        tvShopName.text = shopName
        tvShopAddress.text = shopAddress
        tvShopOpen.text = shopOpen.ifEmpty { "情報なし" }

        Glide.with(this)
            .load(shopImage)
            .into(ivShopImage)

        // ボタンを押すとホットペッパーの店舗ページをブラウザで開く
        btnOpenUrl.setOnClickListener {
            if (shopUrl.isNotEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)))
            }
        }

        // ボタンを押すとGoogleマップで店舗の位置を開く（アプリがなければブラウザで開く）
        btnOpenMap.setOnClickListener {
            val mapUrl = "https://www.google.com/maps/search/?api=1&query=$shopLat,$shopLng"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl)))
        }
    }
}