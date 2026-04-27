package br.com.biptag.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.biptag.model.Inventory
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Insert
    fun save(inventory: Inventory)

    @Delete
    fun delete(inventory: Inventory): Int

    @Update
    fun update(inventory: Inventory): Int

    @Query("SELECT * FROM tb_inventory WHERE id = :id LIMIT 1")
    fun getById(id: Int): Inventory?

    @Query("SELECT * FROM tb_inventory WHERE userId = :userId")
    fun getAllByUserId(userId: Int): Flow<List<Inventory>>
}