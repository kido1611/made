package id.kido1611.dicoding.moviecatalogue3.network

import id.kido1611.dicoding.moviecatalogue3.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private val client: OkHttpClient = buildClient()
        private val retrofit: Retrofit = buildRetrofit(client)

        private const val BASE_URL: String = "https://api.themoviedb.org/3/"

        private fun buildClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor {
                    val url = it.request()
                        .url
                        .newBuilder()
                        .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                        .build()

                    val request = it.request().newBuilder().url(url).build()

                    it.proceed(request)
                }
            if (BuildConfig.DEBUG) {
                builder = builder.addInterceptor(loggingInterceptor)
            }
            return builder.build()
        }

        private fun buildRetrofit(httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(service: Class<T>): T {
            return retrofit.create(service)
        }

    }
}