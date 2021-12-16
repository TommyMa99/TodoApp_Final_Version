package edu.neu.khoury.madsea.majianqing

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoInfo_table")
    fun getList(): Flow<List<TodoEntity>>

    @Insert()
    suspend fun insert(todo: TodoEntity)

    @Delete()
    suspend fun delete(todo: TodoEntity)

    @Update()
    suspend fun update(todo: TodoEntity)
}