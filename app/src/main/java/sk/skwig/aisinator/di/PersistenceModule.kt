package sk.skwig.aisinator.di

import androidx.room.Room
import sk.skwig.aisinator.APP_DATABASE_NAME
import sk.skwig.aisinator.AisinatorDatabase
import javax.inject.Singleton

class PersistenceModule {

    @Singleton
    val appDatabase: AisinatorDatabase by lazy {
        Room.databaseBuilder(context, AisinatorDatabase::class.java, APP_DATABASE_NAME)
            .build()
    }
}
