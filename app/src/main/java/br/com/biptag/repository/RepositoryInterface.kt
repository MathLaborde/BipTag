package br.com.biptag.repository

interface RepositoryInterface<T> {
    suspend fun save(entity: T)
    suspend fun getAll(): List<T>
    suspend fun getById(id: Int = 0): T?
    suspend fun update(entity: T): Int
    suspend fun delete(entity: T): Int
}