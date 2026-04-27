package br.com.biptag.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_user",
)
class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)