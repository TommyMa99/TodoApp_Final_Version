package edu.neu.khoury.madsea.majianqing

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val users: LiveData<List<TodoEntity>> = repository.todos.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(todo: TodoEntity) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: TodoEntity) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: TodoEntity) = viewModelScope.launch {
        repository.delete(todo)
    }
}

class WordViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}