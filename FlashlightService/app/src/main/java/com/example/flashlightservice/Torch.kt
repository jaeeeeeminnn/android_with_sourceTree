package com.example.flashlightservice

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

/**
 * Torch class
 *
 * 손전등 기능을 mainAcitivity에서 분리하여 구현.
 *
 * 손전등 기능을 Service(백그라운드)와 Activity(포그라운드) 모두에서 구현하기 위함.
 */

// 플래시를 켜기 위한 CameraManager 객체를 context로 받아야 함.
class Torch(context: Context) {
    // 카메라를 켜고 끌 때 카메라 id 필요.
    private var cameraId: String? = null
    // getSystemService() : 안드로이드 제공 서비스 매니저 클래스 생성
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE)
            as CameraManager

    init {
        cameraId = getCameraId()
    }

    private fun getCameraId(): String? {
        val cameraIds = cameraManager.cameraIdList
        for (id in cameraIds) {
            // 각 카메라 id별로 세부정보를 가지는 객체 반환.
            val info = cameraManager.getCameraCharacteristics(id)
            val flashAvailable = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            if (flashAvailable != null
                && flashAvailable
                && lensFacing != null
                && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }
        return null
    }

    fun flashOn() {
        cameraManager.setTorchMode(cameraId!!, true)
    }

    fun flashOff() {
        cameraManager.setTorchMode(cameraId!!, false)
    }
}