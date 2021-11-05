package com.example.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
// 앱 위젯용 파일은 AppWidgetProvider라는 브로드캐스트 리시버 클래스 상속
class TorchAppWidget : AppWidgetProvider() {
    // onUpdate() 메소드는 위젯이 업데이트되어야 할 때 호출
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 위젯이 여러 개 배치되었다면 모든 위젯을 업데이트
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // 위젯이 처음 생성될 때 호출
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    // 위젯이 여러 개일 경우, 마지막 위젯이 제거될 때 호출
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

// 위젯을 업데이트할 때 수행되는 코드 
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // 위젯에 배치하는 RemoteViews 객체로 레이아웃을 다룸. 
    // RemoteView 객체 구성
    val views = RemoteViews(context.packageName, R.layout.torch_app_widget)
    // setTextViewText() 메소드는 RemoteViews 객체용으로 준비된 텍스트 값 변경 메소드
    views.setTextViewText(R.id.appwidget_text, widgetText)
    
    // 위젯을 클릭했을 때의 처리 추가 부분

    // 실행할 Intent 작성 (서비스 인텐트)
    val intent = Intent(context, TorchService::class.java)
    // TorchService 서비스를 실행하는 데 PendingIntent.getService() 메소드 사용
    // getService(컨텍스트, 리퀘스트 코드, 서비스 인텐트, 플래그)
    // 리퀘스트 코드, 플래그는 사용하지 않기 때문에 0을 전달.
    val pendingIntent = PendingIntent.getService(context, 0, intent, 0)

    // 레이아웃을 모두 수정했다면 AppWidgetProvider를 사용해 위젯 업데이트
    appWidgetManager.updateAppWidget(appWidgetId, views)
}