package io.github.rebeccacalabretta.teamops.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.rebeccacalabretta.teamops.data.db.AppDatabase
import io.github.rebeccacalabretta.teamops.data.db.ObjectDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "teamops.db"
        ).build()

    @Provides
    fun providePunchSessionDao(
        db: AppDatabase
    ): PunchSessionDao = db.punchSessionDao()

    @Provides
    @Singleton
    fun providePunchSessionRepository(
        dao: PunchSessionDao
    ): PunchSessionRepository =
        PunchSessionRepositoryImpl(dao)

    @Provides
    fun provideObjectDao(
        db: AppDatabase
    ) : ObjectDao = db.objectDao()
}