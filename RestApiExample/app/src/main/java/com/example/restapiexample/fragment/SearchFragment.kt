package com.example.restapiexample.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restapiexample.DEFINES
import com.example.restapiexample.R
import com.example.restapiexample.adapter.SearchResultAdapter
import com.example.restapiexample.model.SearchResultModel
import com.example.restapiexample.rest.HttpTask
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
import java.util.*


class SearchFragment : Fragment() {
    private lateinit var mAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        mAdapter = SearchResultAdapter()
        view.fragment_search_recycler_list.layoutManager = LinearLayoutManager(requireContext())
        view.fragment_search_recycler_list.adapter = mAdapter

        view.fragment_search_button.setOnClickListener {
            doSearch(view.fragment_search_text.text.toString())
        }

        return view
    }

    private fun doSearch(nickName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            var nameResponse: String? = null

            //로딩 시작
            fragment_search_loading.visibility = View.VISIBLE

            //유저 고유 정보 조회
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                val url = HttpTask.USER_INFO_URL
                nameResponse = HttpTask.getOrNull(
                    url.replace("{nickname}", nickName)
                )
            }

            var dealInfoResponse: String? = null

            //유저 정보를 가져오는데 성공하면
            if (nameResponse != null) {
                //json String 데이터를 파싱한다.
                val parser = JsonParser()
                val element: JsonElement = parser.parse(nameResponse)
                val accessId = element.asJsonObject.get("accessId").asString;

                //파싱한 accessId 정보로 유저가 거래한 목록 10개를 요청한다
                withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                    var url = HttpTask.DEAL_INFO_URL
                    url = url.replace("{accessid}", accessId)
                    url = url.replace("{tradetype}", "buy")
                    url = url.replace("{offset}", "0")
                    url = url.replace("{limit}", "50")
                    dealInfoResponse = HttpTask.getOrNull(url)
                }
            }

            if (dealInfoResponse != null) {
                val jsonArray = JSONTokener(dealInfoResponse).nextValue() as JSONArray
                val arrayListData: ArrayList<SearchResultModel> = arrayListOf()
                val decimalFormat = DecimalFormat("#,###")

                val bitmapImage: ArrayList<Bitmap> = arrayListOf()
                var imageResponse: Bitmap? = null

                for (i in 0 until jsonArray.length()) {
                    //선수이미지를 요청해서 받아온다
                    withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                        var url = HttpTask.IMAGE_SPID_URL
                        url = url.replace("{spid}", jsonArray.getJSONObject(i).getString("spid"))
                        imageResponse = HttpTask.getBitmapOrNull(
                            url,
                            jsonArray.getJSONObject(i).getString("spid")
                        )

                        if (imageResponse != null) {
                            bitmapImage.add(imageResponse!!)
                        }
                    }
                }

                for (i in 0 until jsonArray.length()) {
                    val searchResultModel =
                        SearchResultModel(
                            bitmapImage[i],
                            DEFINES.SPID_META_DATA?.getValue(
                                jsonArray.getJSONObject(i)
                                    .getString("spid")
                            ),
                            decimalFormat
                                .format(
                                    jsonArray.getJSONObject(i)
                                        .getLong("value")
                                ).toString() + "BP"
                        )
                    arrayListData.add(searchResultModel)
                }

                mAdapter.setList(arrayListData)
                //로딩 끝
                fragment_search_loading.visibility = View.GONE
            }
        }
    }


}