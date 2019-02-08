package sk.skwig.aisinator.common.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.common.APP_DATABASE_NAME
import sk.skwig.aisinator.common.AppDatabase
import javax.inject.Singleton

@Module
class PersistenceModule{

    @Singleton
    @Provides
    fun provideAppDatabase(context:Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .build()
    }
}