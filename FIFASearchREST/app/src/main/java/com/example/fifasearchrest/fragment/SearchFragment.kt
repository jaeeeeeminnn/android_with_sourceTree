package com.example.fifasearchrest.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fifasearchrest.DEFINES
import com.example.fifasearchrest.R
import com.example.fifasearchrest.adapter.SearchResultAdapter
import com.example.fifasearchrest.model.SearchResultModel
import com.example.fifasearchrest.rest.HttpTask
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.text.DecimalFormat

class SearchFragment : Fragment() {

    private lateinit var mAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 우선 view(fragment_search.xml)부터 inflate 해놓고
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // adapter를 생성.
        mAdapter = SearchResultAdapter()

        // Recycler view를 LayoutManager로 inflate
        view.fragment_search_list.layoutManager = LinearLayoutManager(requireContext())
        // Recycler view에 adapter 선언
        view.fragment_search_list.adapter = mAdapter

        // fragment_search.xml에 있는 button click listener를 선언
        view.fragment_search_button.setOnClickListener {
            // 버튼을 눌렀으면 검색을 할 수 있도록 한다.
            // 검색은 버튼 바로 옆에 editText view를 이용한다.
            doSearch(view.fragment_search_text.text.toString())
        }
        return view
    }

    // http + rest api + nickName 을 이용하여 json을 가져옴.
    // 여기서 json 파싱까지 구현
    private fun doSearch(nickName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            var nameResponse: String? = null

            // 로딩 시작
            framgent_search_loading.visibility = View.VISIBLE

            // 유저 정보 조회
            // 여기부터 비동기
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                // HttpTask.object에 있는 유저정보 url을 습득
                val url = HttpTask.USER_INFO_URL
                // 유저정보를 getOrNull 함수로 받음.
                nameResponse = HttpTask.getOrNull(
                    // url에 있는 nickName value를 editText에 입력한 nickName으로 변경
                    // nickName은 그냥 String이지 변수가 아니므로 $를 붙이지 않는다.
                    url.replace("{nickname}", nickName)
                )
            }
            var dealInfoResponse: String? = null

            // 유저 정보를 가져오는데 성공하면
            if (nameResponse != null) {
                // json 응답 (String)을 파싱한다.
                val parser = JsonParser()
                val element: JsonElement = parser.parse(nameResponse)
                val accessId = element.asJsonObject.get("accessId").asString

                withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                    // 이제는 내가 원하는 정보 (다룰 정보 = DEAL)를 HttpTask로 얻는다.
                    var url = HttpTask.DEAL_INFO_URL

                    // 검색할 url을 내가 원하는 정보로 치환한다.
                    url = url.replace("{accessid}", accessId)
                    url = url.replace("{tradetype}", "buy")
                    url = url.replace("{offset}", "0")
                    url = url.replace("{limit}", "50")

                    // 치환한 url을 가지고 DEAL_INFO를 json으로 가져옴.
                    dealInfoResponse = HttpTask.getOrNull(url)
                }

            }

            // 이제 다룰 정보를 가져왔으면
            // 이미지, 선수이름, 가격을 표현하도록 json을 파싱하고
            // 파싱한 정보를 Recycler view에 반영
            if (dealInfoResponse != null) {
                // json 응답이 배열로 올 것을 예상해 만든 jsonArray
                val jsonArray = JSONTokener(dealInfoResponse).nextValue() as JSONArray
                // fragment에서 Recycler에 넘겨줄 list
                val arrayListData: ArrayList<SearchResultModel> = arrayListOf()
                // 가격 정보 표시 형식
                val decimalFormat = DecimalFormat("#,##0")

                // bitmap 배열
                val bitmapImage: ArrayList<Bitmap> = arrayListOf()
                // 비트맵 이미지는 null (없음.) 일 수도 있다.
                var imageResponse: Bitmap? = null

                for (i in 0 until jsonArray.length()) {
                    // 선수 이미지를 요청해서 받아온다.
                    // IMAGE_SPID_URL + HttpTask 이용
                    withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                        var url = HttpTask.IMAGE_SPID_URL

                        // 비트맵을 얻기 위한 선수 아이디
                        var spid = jsonArray.getJSONObject(i).getString("spid")

                        // url 치환
                        url = url.replace("{spid}", spid)

                        // 받은 jsonObject를 이용하여 imageResponse를 얻음.
                        imageResponse = HttpTask.getBitmapOrNull(url, spid)

                        // 받은 이미지가 있다면 bitmap list에 추가.
                        if (imageResponse != null) {
                            bitmapImage.add(imageResponse!!)
                        }
                    }
                }
                // 이제 Recycler view에 넣은 모든 데이터가 준비되었으므로
                // SearchResultModel 타입으로 list에 넣음.
                for (i in 0 until jsonArray.length()) {
                    val searchResultModel =
                        SearchResultModel(
                            bitmapImage[i],
                            DEFINES.SPID_META_DATA?.getValue(
                                jsonArray.getJSONObject(i)
                                    .getString("spid")
                            ),
                            decimalFormat.format(
                                jsonArray.getJSONObject(i).getLong("value")
                            ).toString() + "BP"
                        )
                    arrayListData.add(searchResultModel)
                }

                // 어뎁터에 list 넘김
                mAdapter.setList(arrayListData)
                // 로딩 끝
                framgent_search_loading.visibility = View.GONE
            }
        }
    }
}
