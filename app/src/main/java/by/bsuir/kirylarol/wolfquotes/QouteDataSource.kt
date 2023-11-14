package by.bsuir.kirylarol.wolfquotes

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import java.util.UUID

interface QuoteDataSource {
    fun getQuotes() : Flow<List<Quote>>
    fun getQuote (id: UUID) : Flow<Quote?>

    suspend fun upsert (quote : Quote)
    suspend fun delete (id : UUID)
    suspend fun setDone (id: UUID)

}

object InMemoryDataSource : QuoteDataSource {

    private val DefaultQuotes = setOf(
        Quote(
            "Сделать сто подтягиваний", "Все понятно?", LocalDate.of(2023, 6, 6),
            LocalDate.of(2023, 7, 6), true
        ),
        Quote(
            "Сделать двесте подтягиваний",
            "Пора ставить себе новые рекорды",
            LocalDate.of(2023, 10, 8),
            LocalDate.of(2023, 7, 6),
            false
        ),
    )

    private val quotes = DefaultQuotes.associateBy { it.id }.toMutableMap()

    private val _quotesFlow = MutableSharedFlow<Map<UUID, Quote>>()

    override fun getQuote(id: UUID): Flow<Quote?> = _quotesFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(quotes)
        }
        .map { it[id] }

    override fun getQuotes(): Flow<List<Quote>> = _quotesFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(quotes)
        }
        .map { it.values.toList() }

    override suspend fun upsert(quote: Quote) {
        delay(1000L)
        quotes[quote.id] = quote
        _quotesFlow.emit(quotes)
    }

    override suspend fun delete(id: UUID) {
        delay(1000L)
        quotes.remove(id)
        _quotesFlow.emit(quotes)
    }

    override suspend fun setDone(id: UUID) {
        delay(100L)
        var quote = quotes[id];
        if (quote != null) {
            quote.completed = !quote.completed

            quotes[id]= quote;
        }
        _quotesFlow.emit(quotes)
    }
}