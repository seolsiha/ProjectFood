package com.example.projectfood

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup

class SearchFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // チップのID → APIに送るrange値(1〜5)の対応表
    private val chipToRange = mapOf(
        R.id.chip300 to 1,
        R.id.chip500 to 2,
        R.id.chip1000 to 3,
        R.id.chip2000 to 4,
        R.id.chip3000 to 5
    )

    // 位置情報の権限リクエスト結果を処理する
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocationAndSearch()
        } else {
            showStatus("位置情報の権限が必要です。")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        view.findViewById<MaterialButton>(R.id.btnSearch).setOnClickListener {
            checkPermissionAndSearch()
        }
    }

    private fun checkPermissionAndSearch() {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            getCurrentLocationAndSearch()
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getCurrentLocationAndSearch() {
        showStatus("位置情報を取得中...")

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    // 選択中のチップからrange値を取得（未選択なら3=1000m）
                    val chipGroup = view?.findViewById<ChipGroup>(R.id.chipGroupRange)
                    val range = chipToRange[chipGroup?.checkedChipId] ?: 3

                    // テスト用：東京駅の座標（提出前に location.latitude / longitude に戻す）
                    val bundle = bundleOf(
                        "lat" to location.latitude,
                        "lng" to location.longitude,
                        "range" to range
                    )
                    findNavController().navigate(
                        R.id.action_searchFragment_to_resultFragment,
                        bundle
                    )
                } else {
                    showStatus("位置情報を取得できませんでした。GPSをオンにしてください。")
                }
            }
            .addOnFailureListener {
                showStatus("位置情報の取得に失敗しました: ${it.message}")
            }
    }

    private fun showStatus(message: String) {
        view?.findViewById<TextView>(R.id.tvStatus)?.text = message
    }
}