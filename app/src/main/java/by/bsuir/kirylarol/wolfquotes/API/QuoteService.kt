package by.bsuir.kirylarol.wolfquotes.API

import by.bsuir.kirylarol.wolfquotes.DAO.QuoteExternal
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

import kotlinx.serialization.Serializable


interface QuoteService {
    suspend fun getQuote(): Quote
}


object HTTP_ROUTES {
    private const val BASE = "https://zenquotes.io/api"
    const val getQuote = "$BASE/random/[your_key]"
}

@Serializable
data class Data(val a: Int, val b: String)

class QuoteServiceImpl(
    private val client: HttpClient
) : QuoteService {
    override suspend fun getQuote(): Quote {
        try {

       var al = client.get {
           url(HTTP_ROUTES.getQuote)
         }.body<List<QuoteExternal>>()[0]
            return Quote(
                title = al.q,
                author = al.a,
            )
        } catch (e: Exception) {
            println(e.message)
            throw Exception("Alalala");
        }
    }
}