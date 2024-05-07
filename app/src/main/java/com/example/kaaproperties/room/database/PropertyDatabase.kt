package com.example.kaaproperties.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant

@Database(
    entities = [
        location::class,
        property::class,
        tenant::class,
    ],
    version = 2
)
abstract class PropertyDatabase: RoomDatabase() {
    abstract val propertyDao: PropertyDao

    companion object{
        @Volatile
        private var INSTANCE: PropertyDatabase ?= null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add the new column 'hasPaid' to the 'tenant' table
                db.execSQL("ALTER TABLE tenant ADD COLUMN hasPaid INTEGER NOT NULL DEFAULT 0")
            }
        }
        fun getInstance(context: Context): PropertyDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "Property_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}