package by.bsuir.kirylarol.wolfquotes.API

import com.ramcosta.composedestinations.BuildConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DataConversion
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.ExperimentalSerializationApi


@OptIn(ExperimentalSerializationApi::class)
fun QuoteHttpClient() = HttpClient(CIO) {
    install(Logging){
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
    install(ContentNegotiation){
        json(Json{
            explicitNulls = false
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = BuildConfig.DEBUG
            coerceInputValues = true
        })
    }
   install(Auth){

   }
   install(HttpRequestRetry){
       retryOnServerErrors(maxRetries = 3)
       exponentialDelay()
   }
   install(HttpTimeout){

   }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    install(HttpCache){

    }
   install(DataConversion){

   }
    install(ContentEncoding){

   }

    /*
    addDefaultResponseValidation(){

    }

     */
    developmentMode = BuildConfig.DEBUG
    expectSuccess = true
    followRedirects = true
    /*
    engine {
        pipelining = true
        endpoint { }
    }

     */
}

