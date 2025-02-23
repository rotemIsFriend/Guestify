package com.example.guestify.di

import android.content.Context
import com.example.guestify.data.local_db.EventDataBase
import com.example.guestify.data.local_db.EventDao
import com.example.guestify.data.local_db.GuestDao
import com.example.guestify.data.repository.EventRepository
import com.example.guestify.data.repository.EventRepositoryFirebase
import com.example.guestify.data.repository.EventRepositoryInterface
import com.example.guestify.data.repository.GuestRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EventDataBase {
        return EventDataBase.getDatabase(context)
    }

    @Provides
    fun provideEventDao(eventDataBase: EventDataBase): EventDao {
        return eventDataBase.eventDao()
    }

    @Provides
    fun provideGuestDao(database: EventDataBase): GuestDao {
        return database.guestDao()
    }

    @Provides
    @Named("Room")
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }

    @Provides
    fun provideGuestRepository(guestDao: GuestDao): GuestRepository {
        return GuestRepository(guestDao)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }



    @Provides
    @Singleton
    @Named("Firestore")
    fun provideFireStoreEventRepository(firestore: FirebaseFirestore, auth: FirebaseAuth): EventRepositoryFirebase {
        return EventRepositoryFirebase(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance() // ✅ Now Hilt can inject FirebaseAuth
    }


}
