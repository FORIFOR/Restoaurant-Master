package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restomaster.data.local.entity.MasterMenuItemAllergen

@Dao
interface MasterMenuItemAllergenDao {
    @Query("SELECT * FROM master_menu_item_allergens WHERE menuItemId = :menuItemId")
    suspend fun getAllergensForMenu(menuItemId: Long): List<MasterMenuItemAllergen>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(relations: List<MasterMenuItemAllergen>)

    @Query("DELETE FROM master_menu_item_allergens WHERE menuItemId = :menuItemId")
    suspend fun deleteAllForMenu(menuItemId: Long)
}
