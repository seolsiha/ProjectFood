package com.example.projectfood.data

// APIのレスポンス全体を表すクラス
data class GourmetResponse(
    val results: Results
)

data class Results(
    val api_version: String,
    val results_available: Int,
    val results_returned: String,
    val results_start: Int,
    val shop: List<Shop>
)

// 1つの店舗情報を表すクラス
data class Shop(
    val id: String,
    val name: String,
    val address: String,
    val access: String,
    val open: String,
    val lat: Double,
    val lng: Double,
    val genre: Genre,
    val photo: Photo,
    val urls: Urls
)

data class Genre(
    val name: String
)

data class Photo(
    val pc: PhotoPc
)

data class PhotoPc(
    val l: String, // 大サイズ画像
    val m: String, // 中サイズ画像
    val s: String  // 小サイズ画像
)

data class Urls(
    val pc: String
)