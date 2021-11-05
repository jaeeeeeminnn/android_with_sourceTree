package com.example.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View

/*  커스텀 뷰 작성하기

    1. View 클래스를 상속받는 새로운 클래스를 생성합니다.
    2. 필요한 메소드를 오버드라이브 (onDraw() 메소드)
 */

// 1. View 클래스를 상속받는 새로운 클래스 생성
// View 클래스의 생성자 중 최소 한 개를 오버드라이브 해야 함.
// View 클래스의 Context 를 인자로 받는 생성자를 선택함.
class TiltView(context: Context?) : View(context) {

    /*  Canvas와 Paint

        Canvas  : 도화지 (뷰의 표면)
        Paint   : 붓 (색, 굵기, 스타일 정의)
     */
    // Paint 객체 선언
    private val greenPaint: Paint = Paint()
    private val blackPaint: Paint = Paint()

    // 원과 동그라미의 중점 좌표
    private var cx: Float = 0f
    private var cy: Float = 0f

    // 센서값을 뷰에 반영
    private var xCoord : Float = 0f
    private var yCoord : Float = 0f


    // Paint 객체의 Color, Style을 변경할 수 있다.
    // Color(default: black)
    // Style(FILL;색 채우기 / FILL_AND_STROKE;획 유지+색 채우기 / STROKE; 획 유지 + 외곽선만)
    init {
        // 녹색 페인트
        greenPaint.color = Color.GREEN
        // 검은색 테두리 페인트
        blackPaint.style = Paint.Style.STROKE
    }

    /*
        w   : 변경된 가로 길이
        h   : 변경된 세로 길이
        oldw: 변경 전 가로 길이
        oldh: 변경 전 세로 길이
     */
    // 뷰의 크기가 변경될 때 호출
    // 중점은 변경된 사이즈의 1/2 지점.
    // 뷰의 크기가 결정되면 중점 좌표를 계산한다는 것.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cx = w / 2f
        cy = h / 2f
    }

    // 인자로 인자로 넘어오는 Canvas 객체에 뷰의 모습을 그림.
    // 도화지가 있어야 그림을 그릴 수 있으므로 Canvas 객체를 인자로 받는 것. 
    override fun onDraw(canvas: Canvas?) {
        // drawCircle(cx: Float, cy: Float, radius: Float, paint: Paint!) :

        // 바깥 원 (검은 테두리 원)
        canvas?.drawCircle(cx, cy, 100f, blackPaint)
        // 색칠 원 (녹색 채워진 원)
        // 액티비티에서 센서값이 변경될 때마다 TiltView이 onSensorEvent() 메소드 호출.
        canvas?.drawCircle(yCoord + cx, xCoord + cy, 100f, greenPaint)

        // 가운데 십자가
        canvas?.drawLine(cx - 20f, cy, cx + 20f, cy, blackPaint)
        canvas?.drawLine(cx, cy - 20f, cx, cy + 20f, blackPaint)
    }

    // onSensorEvent() : SensorEvent를 인자로 받아 새로운 중점을 구하고 뷰를 다시 그림.
    fun onSensorEvent(event : SensorEvent) {
        // 화면을 가로로 돌렸으므로 x축과 y축에 x20을 하여 새로운 중점을 구함.
        xCoord = event.values[0] * 20
        yCoord = event.values[1] * 20

        // invalidate() : 뷰의 onDraw() 메소드를 다시 호출하는 메소드 (뷰를 다시 그림.)
        invalidate()
    }
}