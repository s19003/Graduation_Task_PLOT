package com.example.graduationtaskplot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduationtaskplot.realm.RealmData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    // Realm
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ナビゲーション
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_data,
                    R.id.navigation_fix
                )
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Realm
        realm = Realm.getDefaultInstance()

        // カウントボタン
        findViewById<FloatingActionButton>(R.id.count_button)?.setOnClickListener {
            val intent = Intent(this, CountActivity::class.java)

            // 比較用の時刻を送る
            var day = if (!realm.isEmpty) {
                realm.where(RealmData::class.java).findAll()
                    .sort("date", Sort.DESCENDING).first()?.day
            } else {
                "0"
            }

            intent.putExtra("day", day)

            startActivity(intent)
        }
    }
}