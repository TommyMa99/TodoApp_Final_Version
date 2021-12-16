package edu.neu.khoury.madsea.majianqing

interface UpdateandDelete {

    fun modifyItem(item: TodoEntity)
    fun onItemDelete(item: TodoEntity)
    fun onItemEdit(item: TodoEntity)
    fun onOptionsMenuClicked(position: Int, item: TodoEntity)
}