package br.com.biptag.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.biptag.model.Inventory
import br.com.biptag.model.User

@Database(entities = [User::class, Inventory::class], version = 1)
abstract class BipTagDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao

    companion object {
        private lateinit var instance: BipTagDatabase

        fun getInstance(context: Context): BipTagDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    BipTagDatabase::class.java,
                    "biptag_database"
                ).build()
            }

            return instance
        }

    }
}