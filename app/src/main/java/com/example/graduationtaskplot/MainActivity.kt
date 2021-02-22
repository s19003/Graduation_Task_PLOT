package com.example.graduationtaskplot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduationtaskplot.realm.RealmData
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import io.realm.Sort

class MainActivity : AppCompatActivity() {
    // Realmデータベース
    private lateinit var realm: Realm

    // その他
    private var day: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ボトムナビゲーションの設定
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

        // Realmデータベース
        realm = Realm.getDefaultInstance()

        findViewById<Button>(R.id.count_button)?.setOnClickListener {
            val intent = Intent(this, CountActivity::class.java)

            // 比較用の時刻を取得
            day = if (!realm.isEmpty) {
                realm.where(RealmData::class.java).sort("date", Sort.DESCENDING).findFirst()?.day
            } else {
                "0"
            }
            println("------------------------------------------------------------------------------------------------------------------")
            println(realm.where(RealmData::class.java).findAll())
            println(day)

            intent.putExtra("day", day)
            startActivity(intent)
        }
    }
}