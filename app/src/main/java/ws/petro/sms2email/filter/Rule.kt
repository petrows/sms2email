package ws.petro.sms2email.filter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rule(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "prio") val prio: Int,
    @ColumnInfo(name = "sim") val sim: Int,
    @ColumnInfo(name = "to_email") val toEmail: String,
)
