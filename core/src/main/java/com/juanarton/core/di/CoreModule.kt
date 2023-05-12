package com.juanarton.core.di

import androidx.room.Room
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.repository.TMDBRepository
import com.juanarton.core.data.source.local.LocalDataSource
import com.juanarton.core.data.source.local.room.FavoriteDatabase
import com.juanarton.core.data.source.remote.RemoteDataSource
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { RemoteDataSource() }
    single { LocalDataSource(get())}
    single<TMDBRepositoryInterface> {
        TMDBRepository(
            get(),
            get()
        )
    }
}

val databaseModule = module {
    factory { get<FavoriteDatabase>().dbDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("SharkBite-Hoo-ha-ha".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FavoriteDatabase::class.java, "favorite.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}
