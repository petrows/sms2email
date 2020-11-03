package ws.petro.sms2email.filter

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RuleDao {
    @Insert(onConflict = REPLACE)
    fun save(rule: Rule)

    @Query("SELECT * FROM rule ORDER BY prio ASC")
    fun getAll(): LiveData<List<Rule>>

    @Query("SELECT * FROM rule WHERE id = :ruleId")
    fun load(ruleId: Int): LiveData<Rule>

    @Query("DELETE FROM rule")
    fun deleteAll()
}
