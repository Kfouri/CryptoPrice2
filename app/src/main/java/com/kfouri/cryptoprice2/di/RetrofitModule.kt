package com.kfouri.cryptoprice2.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kfouri.cryptoprice2.data.network.service.CurrencyService
import com.kfouri.cryptoprice2.data.network.utils.NetworkConstants.BASE_URL
import com.kfouri.cryptoprice2.data.network.utils.NetworkConstants.TIMEOUT_IN_SECONDS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesRequestInterceptor() : Interceptor {
        return  Interceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                    .addHeader("Accept", "Application/JSON")
//                    .addHeader(AUTHORIZATION, AUTHORIZATION_TOKEN)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
            requestInterceptor: Interceptor,
            loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        okHttpClient.readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(requestInterceptor)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
            gson: Gson,
            okHttpClient: OkHttpClient
    ): Retrofit.Builder{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit.Builder): CurrencyService {
        return retrofit.build()
                .create(CurrencyService::class.java)
    }
}