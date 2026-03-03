package io.github.rebeccacalabretta.teamops.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.rebeccacalabretta.teamops.data.db.AppDatabase
import io.github.rebeccacalabretta.teamops.data.db.EmployeeDao
import io.github.rebeccacalabretta.teamops.data.db.ObjectDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.db.ScheduleDao
import io.github.rebeccacalabretta.teamops.data.remote.PunchSessionDataSource
import io.github.rebeccacalabretta.teamops.data.remote.ScheduleDataSource
import io.github.rebeccacalabretta.teamops.data.remote.VacationDataSource
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepository
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepositoryImpl
import io.github.rebeccacalabretta.teamops.data.repository.FirebaseUserRepository
import io.github.rebeccacalabretta.teamops.data.repository.FirebaseVacationRepository
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepositoryImpl
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepositoryImpl
import io.github.rebeccacalabretta.teamops.data.repository.ScheduleRepository
import io.github.rebeccacalabretta.teamops.data.repository.ScheduleRepositoryImpl
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.ApproveVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.CalculateRemainingVacationUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.SubmitVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.location.LocationProvider
import io.github.rebeccacalabretta.teamops.location.LocationProviderImpl
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
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun providePunchSessionDao(
        db: AppDatabase
    ): PunchSessionDao = db.punchSessionDao()

    @Provides
    @Singleton
    fun providePunchSessionRepository(
        dao: PunchSessionDao,
        remote: PunchSessionDataSource
    ): PunchSessionRepository =
        PunchSessionRepositoryImpl(dao, remote)

    @Provides
    fun provideObjectDao(
        db: AppDatabase
    ): ObjectDao = db.objectDao()

    @Provides
    @Singleton
    fun provideObjectRepository(
        dao: ObjectDao
    ): ObjectRepository =
        ObjectRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): LocationProvider =
        LocationProviderImpl(context)

    @Provides
    fun provideEmployeeDao(
        db: AppDatabase
    ): EmployeeDao = db.employeeDao()

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        dao: EmployeeDao
    ): EmployeeRepository =
        EmployeeRepositoryImpl(dao)

    @Provides
    fun provideScheduleDao(
        db: AppDatabase
    ): ScheduleDao = db.scheduleDao()

    @Provides
    @Singleton
    fun provideScheduleRepository(
        dao: ScheduleDao,
        remote: ScheduleDataSource
    ): ScheduleRepository =
        ScheduleRepositoryImpl(dao, remote)

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository =
        FirebaseUserRepository(firestore)

    @Provides
    @Singleton
    fun provideVacationRepository(
        remote: VacationDataSource
    ): VacationRepository =
        FirebaseVacationRepository(remote)

    @Provides
    @Singleton
    fun provideSubmitVacationRequestUseCase(
        repository: VacationRepository
    ): SubmitVacationRequestUseCase =
        SubmitVacationRequestUseCase(repository)

    @Provides
    @Singleton
    fun provideApproveVacationRequestUseCase(
        repository: VacationRepository
    ): ApproveVacationRequestUseCase =
        ApproveVacationRequestUseCase(repository)

    @Provides
    @Singleton
    fun provideCalculateRemainingVacationUseCase():
            CalculateRemainingVacationUseCase =
        CalculateRemainingVacationUseCase()
}