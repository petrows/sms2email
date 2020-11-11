package ws.petro.sms2email.filter

/*
    Based on example: https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin
 */

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RuleDao {
    @Insert(onConflict = REPLACE)
    fun save(rule: Rule)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rule: Rule)

    @Query("SELECT * FROM rule ORDER BY prio ASC")
    fun getAll(): LiveData<List<Rule>>

    @Query("SELECT id FROM rule ORDER BY id DESC")
    fun getLastId(): Int

    @Query("SELECT * FROM rule WHERE id = :ruleId")
    fun load(ruleId: Int): LiveData<Rule>

    @Query("DELETE FROM rule")
    fun deleteAll()
}
