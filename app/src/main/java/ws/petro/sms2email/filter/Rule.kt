package ws.petro.sms2email.filter

/*
    Based on example: https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin
 */

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rule(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "prio") val prio: Int,
    @ColumnInfo(name = "sim") val sim: Int,
    @ColumnInfo(name = "to_email") val toEmail: String,
) {
    constructor(title: String, prio: Int, sim: Int, toEmail: String) : this(0, title, prio, sim, toEmail)
}
