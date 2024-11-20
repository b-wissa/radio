package com.challenge.radio.di

import com.challenge.radio.BuildConfig
import com.challenge.radio.station.api.StationApi
import com.challenge.radio.station.repository.StationsRepository
import com.challenge.radio.station.repository.StationsRepositoryImpl
import com.challenge.radio.station.usecase.GetTopStationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
            ).build()

    @Provides
    @Singleton
    fun providesJson(): Json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType(),
                ),
            ).build()

    @Provides
    fun providesStationApi(retrofit: Retrofit): StationApi = retrofit.create(StationApi::class.java)

    @Provides
    @Singleton
    fun providesStationsRepository(stationApi: StationApi): StationsRepository = StationsRepositoryImpl(stationApi = stationApi)

    @Provides
    fun providesGetTopStationsUseCase(stationsRepository: StationsRepository) =
        GetTopStationsUseCase(stationsRepository = stationsRepository)
}
