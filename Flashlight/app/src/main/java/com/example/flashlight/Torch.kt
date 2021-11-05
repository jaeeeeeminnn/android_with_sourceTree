package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.wifi.aware.Characteristics

// CameraManager 객체를 얻기 위해서는 Context 객체가 필요함.
class Torch(context: Context) {

    // 카메라를 켜고 끌 때 카메라ID가 필요.
    // 카메라ID는 기기에 내장된 카메라마다 고유한 id가 부여.
    private var cameraId: String? = null
    // 플래시를 켜려면 CameraManager 객체가 필요
    // context.getSystemService() 는 안드로이드 시스템 서비스를 관리하는 매니저 클래스 생성
    // Context 클래스에 정의된 서비스를 상수로 지정하여 서비스 매니저 클래스를 생성
    // Object 타입을 반환하기 때문에 as CamerManager로 형 변환을 해준다.
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE)
        as CameraManager

    // 클래스 초기화 때 기기에 내장된 카메라에 고유한 id를 받아야 함.
    init {
        cameraId = getCameraId()
    }

    // 카메라가 없다면 ID가 null일 수 있으므로 String? 타입으로 선언
    private fun getCameraId() : String? {
        // CameraManager는 기기가 가지고 있는 모든 카메라에 대한 정보 목록을 제공.
        val cameraIds = cameraManager.cameraIdList
        // 기기의 모든 카메라를 순회.
        for (id in cameraIds) {
            // 각 id 별로 세부 정보를 가지는 객체를 얻음.
            val info = cameraManager.getCameraCharacteristics(id)
            // 얻은 카메라 세부정보 객체로부터 플래시 가능 여부 확인
            val flashAvailable = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            // 카메라 렌즈 방향 획득
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            // flash가 null 이 아니다 == flash 사용이 가능하다
            if (flashAvailable != null && flashAvailable
                && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                // 카메라가 기기의 뒷면을 향하고 있는 카메라의 ID를 찾았다면
                return id
            }
        }
        // 해당하는 카메라 id를 찾지 못했다면 null을 반환.
        // 에뮬레이터를 쓴다면 카메라가 없기 때문에 null을 반환한다.
        return null
    }

    fun flashOn() {
        cameraManager.setTorchMode(cameraId!!, true)
    }

    fun flashOff() {
        cameraManager.setTorchMode(cameraId!!, false)
    }

}