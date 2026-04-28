package br.com.biptag.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_inventory",
    // Removido temporariamente para teste
    /* foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ], */
    indices = [Index(value = ["userId"])]
)
data class Inventory (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val status: String = "Created", // Created, Verified, Stolen
    @ColumnInfo(name = "fiscal_note") val fiscalNote: ByteArray? = null,
    @ColumnInfo(name = "image") val image: ByteArray? = null,
)