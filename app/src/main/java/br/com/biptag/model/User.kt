package br.com.biptag.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_user",
    indices = [Index(value = ["email"], unique = true)]
)
class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    val email: String = "",
    val password: String = "",
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = "",
    val notifications: Boolean = false,
)