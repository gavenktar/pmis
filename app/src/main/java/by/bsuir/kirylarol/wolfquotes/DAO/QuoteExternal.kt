package by.bsuir.kirylarol.wolfquotes.DAO

import kotlinx.serialization.Serializable


@Serializable
data class QuoteExternal (
    val q : String,
    val a : String,
    val h : String
)