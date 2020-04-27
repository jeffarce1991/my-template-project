package com.jeff.project420.internet

import io.reactivex.Completable
import javax.inject.Inject
import com.jeff.project420.exception.NoInternetException
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class DefaultRxInternet @Inject
constructor(): RxInternet{

    override fun isConnected(): Completable =
        Completable
            .defer {
                Retrofit.Builder()
                    .baseUrl("https://www.google.com")
                    .client(OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(Google::class.java)
                    .homepage()
            }
            .onErrorResumeNext { Completable.error(NoInternetException()) }
            .subscribeOn(Schedulers.io())


    interface Google {

        @GET("")
        fun homepage(@Url url: String = ""): Completable
    }

}
