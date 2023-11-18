package by.bsuir.kirylarol.wolfquotes.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import java.util.UUID


data class Quote(
    @SerialName("q")
    val title: String,

    @SerialName("a")
    val author: String,

    @Transient
    val id: UUID = UUID.randomUUID()


)

