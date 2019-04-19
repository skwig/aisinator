package sk.skwig.aisinator.di

import androidx.room.Room
import sk.skwig.aisinator.APP_DATABASE_NAME
import sk.skwig.aisinator.AisinatorDatabase
import javax.inject.Singleton

class PersistenceModule {

    lateinit var androidModule: AndroidModule

    @Singleton
    val appDatabase: AisinatorDatabase by lazy {
        Room.databaseBuilder(androidModule.applicationContext, AisinatorDatabase::class.java, APP_DATABASE_NAME)
            .build()
    }
}
