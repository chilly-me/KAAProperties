package com.example.kaaproperties.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 1
)
abstract class PropertyDatabase: RoomDatabase() {
    abstract val propertyDao: PropertyDao

    companion object{
        @Volatile
        private var INSTANCE: PropertyDatabase ?= null

        fun getInstance(context: Context): PropertyDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "Property_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}