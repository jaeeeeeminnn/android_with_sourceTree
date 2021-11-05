package com.example.ticketbox

import android.graphics.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var array : ArrayList<Int> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: ArrayList<MovieInfo> = arrayListOf()  // <= empty 상태 Null 이 아닌상태..
        
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_bike_24), "자전거왕 엄복동", 1400, "엄복동", "심파" ))
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_boat_24), "캐러비언의 해적", 1400, "바르보사", "판타지"))
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_bus_24), "타요", 1400, "잼민", "유아용"))
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_car_24), "택시운전수", 1500, "송강호", "시대극"))
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_subway_24), "존윅", 1300, "키아누", "액션"))
        list.add(MovieInfo(getDrawable(R.drawable.ic_baseline_directions_walk_24), "달려라 하니", 1000, "하니", "액션"))

        // adapter를 만든다.
        val adapter = RecyclerAdapter(list)
        // 만들어둔 Array List를 adapter에 전달한다.
        movie_list.adapter = adapter
    }
}