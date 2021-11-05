package com.example.addresslatlngconvertor

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    // FusedLocationProviderClient : 기기의 마지막 위치를 가져오기 위한 클래스
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val corToAddressButton : Button = findViewById(R.id.button1)
        val addressToCorButton : Button = findViewById(R.id.button2)
        val addressTextInput : EditText = findViewById(R.id.input)
        val geocoder = Geocoder(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "위치 권한을 설정해주세요.", Toast.LENGTH_LONG).show()
        } else {
            // 이곳에 위치 관련 코드 추가
            corToAddressButton.setOnClickListener {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    Toast.makeText(this, "주소 : ${address[0].subLocality}", Toast.LENGTH_LONG).show()
                }
                fusedLocationClient.lastLocation.addOnCanceledListener {
                    Toast.makeText(this, "마지막 주소 찾지 못함.", Toast.LENGTH_LONG).show()
                }
            }

            addressToCorButton.setOnClickListener {
                if (addressTextInput == null) {
                    Toast.makeText(this, "찾고자 하는 장소를 입력해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    val cor = geocoder.getFromLocationName(addressTextInput.text.toString(), 1)
                    Toast.makeText(this, "좌표 : ${cor[0].latitude}, ${cor[0].longitude}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}