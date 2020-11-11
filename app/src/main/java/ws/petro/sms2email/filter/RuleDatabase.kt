package ws.petro.sms2email.filter

/*
    Based on example: https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin
 */

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Rule::class], version = 3)
abstract class RuleDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao

    companion object {
        @Volatile
        private var INSTANCE:RuleDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RuleDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RuleDatabase::class.java,
                    "rule_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        fun getDatabaseSync(
            context: Context
        ): RuleDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE != null) { return INSTANCE as RuleDatabase }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RuleDatabase::class.java,
                    "rule_database"
                )
                    .build()
                INSTANCE = instance

                return instance
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.ruleDao())
//                    }
//                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        fun populateDatabase(ruleDao: RuleDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            ruleDao.deleteAll()

            var rule = Rule("Hello", 1, -1, "test@example.com")
            ruleDao.insert(rule)
            rule = Rule("World!", 2, -1, "test@example.com")
            ruleDao.insert(rule)
        }
    }
}
