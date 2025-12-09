package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restomaster.data.local.entity.MasterAllergenEntity

@Dao
interface MasterAllergenDao {
    @Query("SELECT * FROM master_allergens ORDER BY sortOrder ASC")
    suspend fun getAllAllergens(): List<MasterAllergenEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(allergens: List<MasterAllergenEntity>)
}
