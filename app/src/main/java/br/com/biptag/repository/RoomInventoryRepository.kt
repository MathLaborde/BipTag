package br.com.biptag.repository

import android.content.Context
import br.com.biptag.dao.BipTagDatabase
import br.com.biptag.model.Inventory

class RoomInventoryRepository(context: Context): RepositoryInterface<Inventory> {
    private val bipTagDatabase= BipTagDatabase.getInstance(context).inventoryDao()

    override fun save(entity: Inventory) {
        bipTagDatabase.save(entity)
    }

    override fun getById(id: Int): Inventory {
        TODO("Not yet implemented")
    }

    override fun update(entity: Inventory): Int {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Inventory): Int {
        TODO("Not yet implemented")
    }
}