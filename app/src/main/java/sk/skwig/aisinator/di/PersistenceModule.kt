package sk.skwig.aisinator.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.APP_DATABASE_NAME
import sk.skwig.aisinator.AppDatabase
import javax.inject.Singleton

@Module
class PersistenceModule{

    @Singleton
    @Provides
    fun provideAppDatabase(context:Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java,
            APP_DATABASE_NAME
        )
            .build()
    }
}