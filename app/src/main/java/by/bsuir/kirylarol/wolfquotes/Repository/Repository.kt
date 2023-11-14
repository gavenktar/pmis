package by.bsuir.kirylarol.wolfquotes.Repository

import by.bsuir.kirylarol.wolfquotes.DataSource.InMemoryDataSource
import by.bsuir.kirylarol.wolfquotes.DataSource.QuoteDataSource
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

interface QuoteRepository {

    fun getQuotes(): Flow<List<Quote>>
    fun getQuote(id: UUID?): Flow<Quote?>

    suspend fun upsert(quote: Quote)

    suspend fun delete(id: UUID)
    suspend fun setDone (id: UUID)
}

object QuoteRepositoryImpl : QuoteRepository {

    private val dataSource: QuoteDataSource = InMemoryDataSource

    override fun getQuotes(): Flow<List<Quote>> = dataSource.getQuotes()
    override fun getQuote(id: UUID?): Flow<Quote?> = id?.let { dataSource.getQuote(it) } ?: flowOf(null)

    override suspend fun upsert(quote: Quote) = dataSource.upsert(quote)
    override suspend fun delete(id: UUID) = dataSource.delete(id)

    override suspend fun setDone(id: UUID) {
        dataSource.setDone (id)
    }

}