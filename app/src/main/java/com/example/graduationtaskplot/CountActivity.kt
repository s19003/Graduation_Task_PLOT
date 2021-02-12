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
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*
import kotlin.random.Random

class CountActivity : AppCompatActivity(), SensorEventListener {
    // Realmデータベース
    private lateinit var realm: Realm

    // センサー
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var xyzArray = FloatArray(3)

    // その他
    private var count = 0
    private var up = true
    private var startButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        // Realmデータベース
        realm = Realm.getDefaultInstance()

        // センサー
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // スタートボタンのON/OFF
        findViewById<Button>(R.id.start_button).setOnClickListener {
            var buttonText = findViewById<Button>(R.id.start_button)
            if (buttonText.text.equals(getString(R.string.text_start))) {
                buttonText.text = getString(R.string.text_stop)
                startButton = true
            } else {
                buttonText.text = getString(R.string.text_start)
                startButton = false
            }
        }

        // 保存
        findViewById<Button>(R.id.save_button).setOnClickListener {
            realm.executeTransaction {
                val maxId = realm.where<RealmData>().max("id")
                val nextId = (maxId?.toInt() ?: 0) + 1
                val realmData = realm.createObject<RealmData>(nextId)

                realmData.count = Random.nextInt(0, 50)
                realmData.date = Date()
            }
            finish()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, xyzArray, 0, xyzArray.size)
        }

        var xValue = (xyzArray[0] * 10) / 10
        var yValue = (xyzArray[1] * 10) / 10
        var zValue = (xyzArray[2] * 10) / 10

        // カウントアルゴリズム
        if (startButton) {
            if (up) {
                if (xValue > 0.85) {
                    up = false
                }
            } else {
                if (xValue < -0.85) {
                    count++
                    up = true
                }
            }

            findViewById<TextView>(R.id.count_text).text = count.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 使わない
    }

    override fun onResume() {
        super.onResume()
        sensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        realm.close()
    }
}