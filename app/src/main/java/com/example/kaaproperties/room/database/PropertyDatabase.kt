package com.example.kaaproperties.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.payments
import com.example.kaaproperties.room.entities.tenant
import com.example.kaaproperties.room.typeConverters.typeConverter

@Database(
    entities = [
        location::class,
        property::class,
        tenant::class,
        payments::class
    ],
    version = 1
)
@TypeConverters(typeConverter::class)
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
                )

                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}