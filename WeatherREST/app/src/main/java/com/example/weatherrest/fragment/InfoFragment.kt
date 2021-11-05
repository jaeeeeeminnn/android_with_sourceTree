package com.example.weatherrest.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherrest.R
import com.example.weatherrest.adapter.InfoAdapter
import com.example.weatherrest.model.InfoModel
import com.example.weatherrest.rest.FileTask
import com.example.weatherrest.rest.HttpTask
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.StringBuilder

class InfoFragment : Fragment() {

    private lateinit var mAdapter: InfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        mAdapter = InfoAdapter()

        // spinner에 들어갈 데이터 구성
        // course Name 배열
        var spinnerList = arrayOf(
            "남호고택에서의 하룻밤",
            "천년의 비밀을 지닌 고찰에서 캠핑을 하다",
            "밝고 청정한 영양의 산천을 찾아서",
            "켜켜이 쌓인 세월의 아름다움을 찾아 서",
            "속리산이 그려낸 즐거운 나날",
            "청주의 자연에서 배우면서 뒹굴자",
            "캠핑을 즐기며 여유롭게 돌아보는 태 안",
            "캠핑에 문화와 예술을 더하다",
            "백제 땅에 캠핑하다"
        )
        // spinnerList = FileTask.fileToArray(spinnerList)

        // spinner를 위한 Adapter 구성
        val spinner = view.findViewById<Spinner>(R.id.fragment_weather_spinner)

        // ArrayAdapter<>(Context, 목록의 layout, 목록 data)
        var sAdapter = ArrayAdapter(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, spinnerList)
        spinner.adapter = sAdapter

        // spinner 동작 감지 Listener 연결
        spinner.setSelection(1)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 선택한 코스명의 index + 1로 courseNum을 설정
                doSearch((position+1).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        // Recycler view를 LayoutManager로 inflate
        view.fragment_weather_list.layoutManager = LinearLayoutManager(requireContext())
        view.fragment_weather_list.adapter = mAdapter

        // Spinner 검색 방법


        // EditText + Button 검색 방법
        view.fragment_weather_button.setOnClickListener {
            Log.d("COURSE_ID", "선택한 코스아이디 : "
                + "${view.fragment_weather_courseNum.text.toString()}")
            doSearch(view.fragment_weather_courseNum.text.toString())
        }

        return view
    }

    // 코스id를 입력으로 받아서 httptask의 REQ_MESSAGE를 변형해 JSON 받음
    // 여기서 json 파싱도 이뤄짐.
    private fun doSearch(courseNum: String) {
        CoroutineScope(Dispatchers.Main).launch {
            var response : String? = null

            // 로딩 시작 ( 25% )
            fragment_weather_loading.visibility = View.VISIBLE
            fragment_weather_loading.progressBar.setProgress(25)

            // 비동기로 url에 courseNum 파라미터를 삽입하여 httptask
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                var url = HttpTask.REQ_MESSAGE2
                url = url.replace("COURSE_ID=1", "COURSE_ID=$courseNum")
                response = HttpTask.getUrlOrNull(url)
            }

            fragment_weather_loading.progressBar.setProgress(50)

            // 응답이 제대로 오면
            if (response != null) {
                // JSON 응답 파싱
                val data = ((((JsonParser.parseString(response) as JsonObject)
                    .getAsJsonObject("response") as JsonObject)
                    .getAsJsonObject("body") as JsonObject)
                    .getAsJsonObject("items") as JsonObject)
                    //.getAsJsonObject("item") as JsonObject)
                // json 데이터가 잘 도착했음을 확인.
                Log.d("JSON_PARSING", "$data")

                val jsonArray: JsonArray = data.getAsJsonArray("item")
                Log.d("JSON_ARRAY", "$jsonArray")

                fragment_weather_loading.progressBar.setProgress(75)

                // fragment에 코스명 반영
                fragment_weather_courseText.text = jsonArray[0].asJsonObject.get("courseName").asString

                // 만든 jsonArray를 이용하여 list에 넣고 adapter에 전달
                val list: ArrayList<InfoModel> = arrayListOf()

                for (i in 0 until jsonArray.size()) {
                    val curr: JsonObject = jsonArray[i].asJsonObject

                    val infoModel = InfoModel(
                        i,
                        curr.get("thema").asString,
                        curr.get("spotName").asString,
                        curr.get("pop").asInt,
                        curr.get("sky").asString,
                        curr.get("th3").asInt
                    )

                    list.add(infoModel)
                }

                // adapter에 list 전달.
                mAdapter.setList(list)

                // 로딩 완료
                fragment_weather_loading.progressBar.setProgress(95)
                fragment_weather_loading.visibility = View.INVISIBLE
            }
        }
    }
}