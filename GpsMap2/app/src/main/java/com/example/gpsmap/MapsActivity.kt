package com.example.gpsmap

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gpsmap.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.jar.Manifest

/*  GpsMap

    구글 지도에 나의 위치와 이동경로 표시 어플리케이션

    1. 구글지도 표시하기
        a. Google map view로 프로젝트 생성
        b. 의존성 추가 (gradle : Module)
        c. 구글 지도 API 키 발급받기 (google_maps_api.xml)
        d. MapsActivity 기본 코드 분석
    2. 현재 위치 정보 얻기 (주기적으로 수신)
        a. manifest에 위치 권한 추가
        b. onResume() 메소드에서 위치정보 요청
        c. 위치 정보 갱신 콜백 정의
        d. onPause() 메소드에서 위치 정보 요청 중지
    3. 주기적으로 현재 위치 정보 업데이트
    4. 이동 자취를 선으로 그리기
        a. 이동 경로 그리기
        b. 화면 유지하기
        c. 테스트
 */

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // 위치정보를 주기적으로 얻는 데 필요한 객체들을 선언
    // MyLocationCallback 객체는 MapsActivity 클래스의 inner class로 생성.
    /*
        fusedLocationProviderClient

        - 현재 위치 정보를 얻는 클래스
        - requestLocationUpdates() 메소드를 호출해 위치 정보를 요청.

        requestLoactionUpdates(LocationRequest, LocationCallback, Looper)

        - fusedLocationProviderClient이 내장 함수로 위치정보를 요청
        - LocationRequest   : 위치 요청 객체 ( 위치 정보를 요청하는 시간 주기를 설정하는 객체)
        - LocationCallback  : 위치가 갱신되면 호출되는 콜백
        - Looper            : 특정 looper thread를 지정. (특별한 경우가 아니라면, null로 설정)

     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack
    
    // 권한 요청에 관한 분기를 처리를 위한 멤버 변수
    private val REQUEST_ACCESS_FINE_LOCATION = 1000

    // PolyLine 옵션
    // 먼저 PolyLineOptions 객체를 생성. 
    // 굵기 5f, 색상 빨강으로 설정
    private val polyLineOptions = PolylineOptions().width(5f).color(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 세로 모드로 화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // SupportMapFragment를 가져와서 지도가 준비되면 알림 수신.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 지도가 완성 된 후 위치정보를 얻기 위한 초기화
        locationInit()
    }

    /*  locationRequest
    
        - 위치정보 요청에 대한 세부 정보 설정
        - priority : 정확도
        - interval : 위치를 갱신하는 데 필요한 시간을 millisec 단위로 입력
        - fastestInterval : 다른 앱에서 위치를 갱신했을 때 그 정보를 가장 빠른 간격(millisec)으로 입력
    
     */
    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationRequest = LocationRequest()

        locationCallback = MyLocationCallBack()

        // GPS 우선
        // 위치 정보 요청의 정확성은 최대한 높게
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // 위치 정보가 없을 때는 업데이트 x
        // 상황에 따라 짧아질 수 있음. 정확하지는 않음.
        // 다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음.
        locationRequest.interval = 10000
        // 정확함. 이것보다 짧은 업데이트는 하지 않음.
        locationRequest.fastestInterval = 5000
    }

    /**
     * 사용 가능한 맵을 조작
     * 지도를 사용할 준비가 되면 이 콜백이 호출
     * 여기서 마커나 선, 청취자를 추가하거나 카메라를 이동할 수 있음.
     * 호주 시드니 근처에 마커를 추가하고 있음.
     * Google Play 서비스가 기기에 설치되어 있지 않은 경우 사용자에게
     * SupportMapFragment 안에 Google Play 서비스를 설치하라는 메시지가 표시됨.
     * 이 메소드는 사용자가 Google Play 서비스를 설치하고 앱으로 돌아온 후에만 호출(실행)됨.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 시드니에 마커를 추가하고 카메라를 이동시킴.
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onResume() {
        super.onResume()
        // 위치요청은 activity가 활성화되는 onResume() 메소드에서 수행.
        // 위치 권한은 위험 권한이므로 실행 중에 사용자에게 물어봄.
        // cancel   : 권한요청 거부한 적이 있으면 권한이 필요한 이유 설명
        // ok       : 권한요청 수락했다면 현재 위치 정보를 요청
        permissionCheck(cancel = { showPermissionInfoDialog() }, ok = { addLocationListener() })
    }

    // 실행 중 권한 요청 코드를 작성하지 않았으면 빨간 에러 표시가 나타남.
    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    /*  MyLocationCallBack

        requestLoactionUpdates() 메소드에 전달되는 인자 중 LocationCallBack을 구현한 내부 클래스
        LocationResult 객체를 반환.
        lastLocation 속성으로 Location 객체를 얻음.

        주기적으로 위치 정보가 갱신되는지 확인하는 로그 코드 포함.
     */
    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            // lastLocation 속성으로 location 객체를 얻음.
            val location = locationResult?.lastLocation

            // 기기의 GPS 설정이 꺼져 있거나 현재 위치 정보를 얻을 수 없는 경우에 Location 객체가 null일 수 있음.
            // Location 객체가 null 이 아닐 때 해당 위도와 경도 위치로 카메라를 이동.
            location?.run {
                // 14 level 로 확대하고 현재 위치로 카메라 이동
                // latitude / longitude : 위도 / 경도
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                // 3. 주기적으로 위치정보 업데이트 확인하기
                Log.d("MapsActivity", "위도 : $latitude, 경도 : $longitude")

                /*  4. 이동경로 그리기

                    구글지도의 뷰 그래픽 API

                    - addPolyLine() : 선의 집합으로 지도에 경로와 노선을 표시
                    - addCircle()   : 원을 표시
                    - addPolygon()  : 영역을 표시

                 */
                // PolyLine 에 좌표 추가
                // 위치정보가 갱신되면 해당 좌표를 polyLineOption 객체에 추가
                polyLineOptions.add(latLng)

                // 선 그리기
                // 지도에 polyLineOption 객체를 추가
                mMap.addPolyline(polyLineOptions)
            }
        }
    }

    private fun takePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        // 위치 권한이 있는지 검사
        // 위치 권한이 없으면
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 과거에 권한 거부가 있었는지 검사
            // 거부한 적이 있으면
            // sholdShowRequestPermissionRationale() : 과거 거부 여부 반환.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                cancel()
            }
            // 거부한 적이 없으면
            else {
                // 권한 요청
                takePermission()
            }
        }
        // 위치 권한이 있으면
        else {
            ok()
        }
    }

    private fun showPermissionInfoDialog() {
        // 위치정보가 필요한 이유를 설명하는 alertDialog 를 표시.
        // alertDialog 에는 yes / no 버튼이 있음.
        alert("현재 위치 정보를 얻으려면 위치 권한이 필요합니다.", "권한이 필요한 이유") {
            yesButton {
                // 권한 요청
                takePermission()
            }
            noButton {  }
        }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // requestCode : 요청할 권한에 대한 멤버 변수값
        // grantResult : 요청한 권한에 대한 결과 배열 (intArray)
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                // 권한 요청이 있고, 그 요청에 대한 결과가 granted라면
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한 허용됨.
                    addLocationListener()
                }
                else {
                    // 권한 거부됨.
                    toast("권한 거부됨.")
                }
                return
            }
        }
    }

    // 2.d onPause()로 위치정보 요청 권한 삭제
    override fun onPause() {
        super.onPause()

        removeLocationListener()
    }

    private fun removeLocationListener() {
        // 현재 위치 요청을 삭제
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
    // 2.d

}