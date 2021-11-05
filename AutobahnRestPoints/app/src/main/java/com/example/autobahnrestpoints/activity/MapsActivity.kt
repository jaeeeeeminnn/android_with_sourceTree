package com.example.autobahnrestpoints.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autobahnrestpoints.DEFINES as D
import com.example.autobahnrestpoints.R
import com.example.autobahnrestpoints.adapter.RestPointAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.autobahnrestpoints.databinding.ActivityMapsBinding
import com.example.autobahnrestpoints.model.RestPointModel
import com.example.autobahnrestpoints.thread.HttpThread
import com.example.autobahnrestpoints.http.HttpTask as H
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mAdapter : RestPointAdapter

    private lateinit var searchText : EditText
    private lateinit var searchButton : ImageButton
    private lateinit var yearSpinner : Spinner
    private lateinit var monthSpinner : Spinner
    private lateinit var daySpinner : Spinner
    private lateinit var hourSpinner : Spinner
    private lateinit var routeName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAdapter()

        // DB 인스턴스

        // 추후 뷰 바인딩으로 바꾸기
        searchText = findViewById(R.id.maps_search_text)
        searchButton = findViewById(R.id.maps_search_button)
        routeName = findViewById(R.id.maps_routeName)

        spinnerTask()

        // 스피너 응답
        var yearAnswer : String? = null
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                yearAnswer = D.yearList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearAnswer = yearList[0]
            }
        }
        var monthAnswer : String? = null
        monthSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                monthAnswer = monthList[position]
                if (monthAnswer != null && monthAnswer!!.length < 2) {
                    monthAnswer = "0" + monthAnswer
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                monthAnswer = monthList[0]
            }
        }
        var dayAnswer : String? = null
        daySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dayAnswer = dayList[position]
                if (dayAnswer != null && dayAnswer!!.length < 2) {
                    dayAnswer = "0" + dayAnswer
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                dayAnswer = dayList[0]
            }
        }
        var hourAnswer : String = hourList[0]
        hourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hourAnswer = hourList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                hourAnswer = hourList[0]
            }
        }

        val dateAnswer : String = "$yearAnswer$monthAnswer$dayAnswer"
        val spinnerAnswer : ArrayList<String> = arrayListOf(dateAnswer, hourAnswer)


        searchButton.setOnClickListener {
            Toast.makeText(this, "${searchText.text} : ${date[0]}/${date[1]}", Toast.LENGTH_LONG).show()
        }

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.item_map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        // 리스트 클릭

    }

    override fun onStart() {
        super.onStart()

    }

    private fun spinnerTask() {
        this.yearSpinner = findViewById(R.id.maps_year_spinner)
        this.monthSpinner = findViewById(R.id.maps_month_spinner)
        this.daySpinner = findViewById(R.id.maps_day_spinner)
        this.hourSpinner = findViewById(R.id.maps_hour_spinner)

        // 스피너 list 초기화
        val yearList = D.getYearList()
        val monthList = D.getMonthList()
        val dayList = D.getDayList()
        val hourList = D.getHourList()

        // 스피너 adapter 생성 및 등록
        val yearAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, yearList)
        val monthAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, monthList)
        val dayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, dayList)
        val hourAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, hourList)
        yearSpinner.adapter = yearAdapter
        monthSpinner.adapter = monthAdapter
        daySpinner.adapter = dayAdapter
        hourSpinner.adapter = hourAdapter

    }

    private fun loadAdapter() {
        mAdapter = RestPointAdapter()
        val list : ArrayList<RestPointModel> = D.searchResultList
        mAdapter.setList(list)
        findViewById<RecyclerView>(R.id.maps_list).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.maps_list).adapter = mAdapter
    }

    private fun searchRestPoint(route: String, date : String?, hour : String?) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.Default).async {
                thread(start = true) {
                    if (date == null || hour == null) {
                        HttpThread(route, "20200101", "10")
                    } else {
                        HttpThread(route, date, hour).run()
                    }
                }
            }.await()
            mAdapter.setList(D.searchResultList)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}