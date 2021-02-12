package com.example.graduationtaskplot.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class RealmData : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var count: Int = 0
    var date: Date = Date()
}