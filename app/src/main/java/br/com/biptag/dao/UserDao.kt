package br.com.biptag.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.biptag.model.User

@Dao
interface UserDao {

    // O Room agora tem uma instrução para gerar o código!
    @Insert
    fun salvarUsuario(usuario: User) // Substitua 'User' pelo nome da sua classe

    // Exemplo de uma função de busca
    @Query("SELECT * FROM tb_user") // Substitua pelo nome da sua tabela
    fun buscarTodosUsuarios(): List<User>

}