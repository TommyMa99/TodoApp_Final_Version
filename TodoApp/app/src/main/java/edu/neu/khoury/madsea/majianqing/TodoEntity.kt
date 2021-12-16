package edu.neu.khoury.madsea.majianqing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoInfo_table")
class TodoEntity(
    var itemDataText: String,
    var moreDetails: String,
    var tags: String,
    var deadline: String,
    var reminder: Boolean,
    var timeToRemind: String,
    var done: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
