//package br.com.biptag.repository
//
//import android.content.Context
//import br.com.biptag.dao.BipTagDatabase
//import br.com.biptag.model.Inventory
//import kotlinx.coroutines.flow.Flow
//
//class RoomInventoryRepository(context: Context): RepositoryInterface<Inventory> {
//    private val bipTagDatabase= BipTagDatabase.getInstance(context).inventoryDao()
//
//    override suspend fun save(entity: Inventory) {
//        bipTagDatabase.save(entity)
//    }
//
//    override suspend fun getAll(): List<Inventory> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getById(id: Int): Inventory {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun update(entity: Inventory): Int {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun delete(entity: Inventory): Int {
//        TODO("Not yet implemented")
//    }
//
//    fun getAllByUser(id: Int): Flow<List<Inventory>> {
//        return bipTagDatabase.getAllByUserId(id)
//    }
//}
