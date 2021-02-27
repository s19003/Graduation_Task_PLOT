package com.example.graduationtaskplot

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.graduationtaskplot.realm.RealmData
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class CountActivity : AppCompatActivity(), SensorEventListener {
    // Realmデータベース
    private lateinit var realm: Realm
    private lateinit var realmData: RealmQuery<RealmData>

    // 加速度センサー
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var xyzArray = FloatArray(3)

    // 照度センサー
    private var light: Sensor? = null
    private var lightValue = 0f

    // その他
    private var count: Int = 0
    private var day = ""
    private var date = ""
    private var up = true
    private var startButton = true
    private var today = false

    // テスト用
    private var x = 0f
    private var y = 0f
    private var z = 0f
    private var xyz = 0f

//    private var xList = mutableListOf<Float>()
//    private var yList = mutableListOf<Float>()
//    private var zList = mutableListOf<Float>()
//    private var xMinusList = mutableListOf<Float>()
//    private var yMinusList = mutableListOf<Float>()
//    private var zMinusList = mutableListOf<Float>()
//    private var xyzList = mutableListOf<Float>()
//
//    private var xAve = 0f
//    private var yAve = 0f
//    private var zAve = 0f
//    private var xMinusAve = 0f
//    private var yMinusAve = 0f
//    private var zMinusAve = 0f
//    private var xyzAve = 0f
//
//    private var xMax = 0f
//    private var yMax = 0f
//    private var zMax = 0f
//    private var xMinusMax = 0f
//    private var yMinusMax = 0f
//    private var zMinusMax = 0f
//    private var xyzMax = 0f
//
//    private var xCount = 0
//    private var yCount = 0
//    private var zCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        // Realmデータベース
        realm = Realm.getDefaultInstance()
        realmData = realm.where()

        // 加速度センサー
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        // 照度センサー
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Intentから時刻を取得する
        day = intent.getStringExtra("day") ?: ""

        // 現在時刻を取得する
        val current = SimpleDateFormat("yy-MM-dd")
        date = current.format(Date())

        // 今日の測定がある場合、カウントに代入する
        if (day == date) {
            val count = realmData.equalTo("day", day).findAll().firstOrNull()?.count ?: 0
            this.count = count
            findViewById<TextView>(R.id.count_text).text = this.count.toString()

            today = true
        }

        // 計測のON/OFFを行うボタン ON=START OFF=STOP
        val buttonText = findViewById<Button>(R.id.start_button)
        buttonText.setOnClickListener {
            if (buttonText.text.equals(getString(R.string.text_start))) {
                buttonText.text = getString(R.string.text_stop)
                startButton = true
                buttonText.setBackgroundColor(resources.getColor(R.color.purple_700))
            } else {
                buttonText.text = getString(R.string.text_start)
                startButton = false
                buttonText.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
            }
        }

        // カウントを保存する
        findViewById<Button>(R.id.save_button).setOnClickListener {
            when {
                today -> {
                    realm.executeTransaction {
                        val realmNew = realmData.equalTo("day", day).findFirst()
                        realmNew?.count = count
                        realmNew?.date = Date()
                    }
                }
                !today -> {
                    realm.executeTransaction {
                        val maxId = realmData.max("id")
                        val nextId = (maxId?.toInt() ?: 0) + 1
                        val realmData = realm.createObject<RealmData>(nextId)

                        realmData.count = count
                        realmData.date = Date()
                        realmData.day = date
                    }
                }
            }

            finish()
        }

//        findViewById<Button>(R.id.test).setOnClickListener {
//            var builder = StringBuilder()
//
//            builder.append("X_AVE = $xAve \n")
//            builder.append("Y_AVE = $yAve \n")
//            builder.append("Z_AVE = $zAve \n")
//            builder.append("X_MINUS_AVE = $xMinusAve \n")
//            builder.append("Y_MINUS_AVE = $yMinusAve \n")
//            builder.append("Z_MINUS_AVE = $zMinusAve \n")
//            builder.append("XYZ_AVE = $xyzAve \n\n")
//
//            builder.append("X_MAX = $xMax \n")
//            builder.append("Y_MAX = $yMax \n")
//            builder.append("Z_MAX = $zMax \n")
//            builder.append("X_MINUS_MAX = $xMinusMax \n")
//            builder.append("Y_MINUS_MAX = $yMinusMax \n")
//            builder.append("Z_MINUS_MAX = $zMinusMax \n")
//            builder.append("XYZ_MAX = $xyzMax \n\n")
//
//            builder.append("X count = $xCount \n")
//            builder.append("Y count = $yCount \n")
//            builder.append("Z count = $zCount \n")
//
//
//            findViewById<TextView>(R.id.count_text).text = builder
//        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            System.arraycopy(event.values, 0, xyzArray, 0, xyzArray.size)
        } else if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            lightValue = event.values[0]
        }

        x = xyzArray[0]
        y = xyzArray[1]
        z = xyzArray[2]
        xyz = sqrt(x.pow(2) + y.pow(2) + z.pow(2))

        if (startButton) {
//            if (x > 0) xList.add(x) else xMinusList.add(x)
//            if (y > 0) yList.add(y) else yMinusList.add(y)
//            if (z > 0) zList.add(z) else zMinusList.add(z)
//            if (xyz > 0) xyzList.add(xyz)
//
//            xAve = xList.average().toFloat()
//            yAve = yList.average().toFloat()
//            zAve = zList.average().toFloat()
//            xMinusAve = xMinusList.average().toFloat()
//            yMinusAve = yMinusList.average().toFloat()
//            zMinusAve = zMinusList.average().toFloat()
//            xyzAve = xyzList.average().toFloat()
//
//            xMax = xList.maxOrNull() ?: 0f
//            yMax = yList.maxOrNull() ?: 0f
//            zMax = zList.maxOrNull() ?: 0f
//            xMinusMax = xMinusList.minOrNull() ?: 0f
//            yMinusMax = yMinusList.minOrNull() ?: 0f
//            zMinusMax = zMinusList.minOrNull() ?: 0f
//            xyzMax = xyzList.maxOrNull() ?: 0f

//            if (x > 1.5 || x < -1.5) xCount++
//            if (y > 1.5 || y < -1.5) yCount++
//            if (z > 1.5 || z < -1.5) zCount++

            if (up) {
                if ((z > 1.0) && (xyz > 1.5) && (lightValue < 22f)) {
                    up = false
                }
            } else {
                if ((z < -1.0) && (xyz > 1.5) && (lightValue < 22f)) {
                    count++
                    up = true
                }
            }

//            val hoge = if (count < 4) 0 else 5

            findViewById<TextView>(R.id.count_text).text = count.toString()
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        sensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        light?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        realm.close()
    }
}