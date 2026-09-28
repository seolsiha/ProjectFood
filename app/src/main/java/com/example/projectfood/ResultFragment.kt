package com.example.projectfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfood.data.RetrofitClient
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val progressBar = view.findViewById<android.widget.ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // SearchFragmentから渡された緯度・経度・範囲を受け取る
        val lat = arguments?.getDouble("lat")
        val lng = arguments?.getDouble("lng")
        val range = arguments?.getInt("range") ?: 3

        if (lat == null || lng == null) {
            tvStatus.text = "位置情報が取得できませんでした。"
            return
        }

        // ローディングスピナーを表示し、状態メッセージは空にする
        progressBar.visibility = View.VISIBLE
        tvStatus.text = ""

        // コルーチンでAPIを呼び出す（非同期処理）
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchShops(
                    key = BuildConfig.HOTPEPPER_API_KEY,
                    lat = lat,
                    lng = lng,
                    range = range
                )

                val shopList = response.results.shop

                // 検索が終わったのでローディングスピナーを隠す
                progressBar.visibility = View.GONE

                if (shopList.isEmpty()) {
                    tvStatus.text = "近くにお店が見つかりませんでした。"
                } else {
                    tvStatus.text = "検索結果：${shopList.size}件"

                    val adapter = ShopAdapter(shopList) { selectedShop ->
                        val bundle = Bundle().apply {
                            putString("shopId", selectedShop.id)
                            putString("shopName", selectedShop.name)
                            putString("shopAddress", selectedShop.address)
                            putString("shopOpen", selectedShop.open)
                            putString("shopImage", selectedShop.photo.pc.l)
                            putString("shopUrl", selectedShop.urls.pc)
                            putDouble("shopLat", selectedShop.lat)
                            putDouble("shopLng", selectedShop.lng)
                        }
                        findNavController().navigate(
                            R.id.action_resultFragment_to_detailFragment,
                            bundle
                        )
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvStatus.text = "エラーが発生しました: ${e.message}"
            }
        }
    }
}