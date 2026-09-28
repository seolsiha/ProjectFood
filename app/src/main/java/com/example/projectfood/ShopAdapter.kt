package com.example.projectfood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfood.data.Shop

// リストの各アイテムがクリックされたときの処理を外部から渡してもらう
class ShopAdapter(
    private val shopList: List<Shop>,
    private val onItemClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
        val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val tvAccess: TextView = view.findViewById(R.id.tvAccess)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.tvShopName.text = shop.name
        holder.tvAccess.text = shop.access

        Glide.with(holder.itemView.context)
            .load(shop.photo.pc.m)
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            onItemClick(shop)
        }
    }

    override fun getItemCount(): Int = shopList.size
}