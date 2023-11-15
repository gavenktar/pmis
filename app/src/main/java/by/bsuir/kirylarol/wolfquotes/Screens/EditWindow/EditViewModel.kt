package by.bsuir.kirylarol.wolftasks.Screens.EditWindow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepository
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepositoryImpl
import by.bsuir.kirylarol.wolftasks.Entity.Task

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import java.util.UUID
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


sealed interface EditViewState {
    data object Loading : EditViewState
    data class Error(val e: Exception) : EditViewState
    data class EditingTask(val title: String,
                            val description: String,
                            val dateCreated: LocalDate,
                            val dateEnd: LocalDate,
                            var completed: Boolean,
                            val id: UUID? = null, val saved: Boolean = false) :
        EditViewState
}

class EditTaskViewModel(
    private val repo: TaskRepository,
    private val id: UUID?,
) : ViewModel() {

    private val saved = MutableStateFlow(false)
    private val loading = MutableStateFlow(false)

    val state = combine(
        repo.getTask(id),
        saved,
        loading,
    ) { task, saved, loading ->
        if (loading) EditViewState.Loading else EditViewState.EditingTask(
            task?.title ?: "",
            task?.description ?: "",
            task?.dateCreated ?: LocalDate.now(),
            task?.dateEnd ?: LocalDate.now(),
            task?.completed ?: false,
            task?.id,
            saved
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), EditViewState.Loading)

    fun onClickSave(
        title : String,
        description : String,
        dateCreated : LocalDate,
        dateEnd : LocalDate,
        completed : Boolean,
        id : UUID?
    ) = viewModelScope.launch {
        loading.update { true }
        repo.upsert(Task(title, description, dateCreated, dateEnd, completed, id ?: UUID.randomUUID()))
        loading.update { false }
        saved.update { true }
    }
}
