package by.bsuir.kirylarol.wolfquotes.DataSource

import by.bsuir.kirylarol.wolfquotes.DAO.QuoteDao
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.Entity.QuoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.UUID

interface QuoteDataSource {
    fun getQuotes(): Flow<List<Quote>>
    fun getQuote(id: UUID): Flow<Quote?>

    suspend fun upsert(quote: Quote)
    suspend fun delete(id: UUID)
}


internal class RoomQuoteDataSource(private val dao: QuoteDao) : QuoteDataSource {


    private val _quotesFlow = MutableSharedFlow<Map<UUID, Quote>>()


    private lateinit var quotes: MutableMap<UUID, Quote>;

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAll().collect { quoteEntities ->
                quotes = quoteEntities.associateBy { it.id }
                    .mapValues { entry ->
                        Quote(
                            id = entry.value.id,
                            title = entry.value.title,
                            author = entry.value.author,
                        )
                    }
                    .toMutableMap()
            }
        }
    }


    override fun getQuote(id: UUID): Flow<Quote?> = _quotesFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(quotes)
        }
        .map { it[id] }


    private fun toQuoteEntity(quote: Quote): QuoteEntity {
        return QuoteEntity(
            id = quote.id,
            title = quote.title,
            author = quote.author
        )
    }

    override fun getQuotes(): Flow<List<Quote>> = _quotesFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(quotes)
        }
        .map { it.values.toList() }

    override suspend fun upsert(quote: Quote) {
        dao.save(toQuoteEntity(quote))
    }

    override suspend fun delete(id: UUID) {
        delay(1000L)
        var quote = quotes[id]
        if (quote != null) {
            dao.delete(toQuoteEntity(quote))
        }
        quotes.remove(id)
        _quotesFlow.emit(quotes)
    }
    
}