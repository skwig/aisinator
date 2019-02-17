package sk.skwig.aisinator.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import sk.skwig.aisinator.APP_DATABASE_NAME
import sk.skwig.aisinator.AisinatorDatabase
import sk.skwig.aisinator.dashboard.CourseDaoProvider
import sk.skwig.aisinator.dashboard.DeadlineDaoProvider
import sk.skwig.aisinator.dashboard.LessonDaoProvider
import javax.inject.Singleton

@Module(includes = [PersistenceBinderModule::class])
class PersistenceModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AisinatorDatabase {
        return Room.databaseBuilder(context, AisinatorDatabase::class.java, APP_DATABASE_NAME)
            .build()
    }
}

@Module
abstract class PersistenceBinderModule {

    @Binds
    abstract fun bindCourseDaoProvider(aisinatorDatabase: AisinatorDatabase): CourseDaoProvider

    @Binds
    abstract fun bindDeadlineDaoProvider(aisinatorDatabase: AisinatorDatabase): DeadlineDaoProvider

    @Binds
    abstract fun bindLessonDaoProvider(aisinatorDatabase: AisinatorDatabase): LessonDaoProvider
}
