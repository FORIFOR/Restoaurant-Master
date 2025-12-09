package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restomaster.data.local.entity.MenuTranslationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuTranslationDao {

    @Query("SELECT * FROM menu_translations WHERE menu_id = :menuId")
    fun getTranslationsForMenu(menuId: Long): Flow<List<MenuTranslationEntity>>

    @Query("SELECT * FROM menu_translations WHERE menu_id = :menuId AND lang = :lang LIMIT 1")
    suspend fun getTranslation(menuId: Long, lang: String): MenuTranslationEntity?

    // For public API: get all approved translations for a specific store and language
    @Query("""
        SELECT T.* FROM menu_translations T 
        INNER JOIN menu_items M ON T.menu_id = M.id 
        WHERE M.storeId = :storeId AND T.lang = :lang AND T.status = 'approved'
    """)
    suspend fun getApprovedTranslationsForStore(storeId: Long, lang: String): List<MenuTranslationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(translation: MenuTranslationEntity)

    @Query("DELETE FROM menu_translations WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM menu_translations WHERE menu_id = :menuId")
    suspend fun deleteAllForMenu(menuId: Long)
}
