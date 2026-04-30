package br.com.biptag.repository

import br.com.biptag.model.User

interface RepositoryInterface<T>{
    fun save(entity: T)
    fun getById(id: Int = 0): T
    fun update(entity: T): Int
    fun delete(entity: T): Int
}