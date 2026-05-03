package br.com.biptag.repository

import android.content.Context
import br.com.biptag.dao.BipTagDatabase
import br.com.biptag.model.User

class RoomUserRepository(context: Context): RepositoryInterface<User>{
    private val bipTagDatabase= BipTagDatabase.getInstance(context).userDao()

    override suspend fun save(entity: User) {
        bipTagDatabase.save(entity)
    }

    override suspend fun getAll(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): User {
        return bipTagDatabase.getById(id)
    }

    override suspend fun update(entity: User): Int {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: User): Int {
        TODO("Not yet implemented")
    }

    fun login(email: String, password: String): User? {
        return bipTagDatabase.login(email, password)
    }
}
