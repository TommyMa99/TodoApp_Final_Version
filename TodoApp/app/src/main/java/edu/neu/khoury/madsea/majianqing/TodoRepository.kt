package edu.neu.khoury.madsea.majianqing

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class TodoRepository(private val todoDao: TodoDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val todos: Flow<List<TodoEntity>> = todoDao.getList()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(todoInfo: TodoEntity) {
        todoDao.insert(todoInfo)
    }

    suspend fun update(todoInfo: TodoEntity) {
        todoDao.update(todoInfo)
    }

    suspend fun delete(todoInfo: TodoEntity) {
        todoDao.delete(todoInfo)
    }
}