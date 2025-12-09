package com.example.restomaster.data.repository

import com.example.restomaster.data.local.AppDatabase
import com.example.restomaster.data.local.entity.AllergenEntity
import com.example.restomaster.data.local.entity.MasterAllergenEntity
import com.example.restomaster.data.local.entity.MasterMenuItemAllergen
import com.example.restomaster.data.local.entity.MenuItemAllergenEntity
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class MenuRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val imageEnhancementApiService: com.example.restomaster.data.remote.ImageEnhancementApiService,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) : MenuRepository {
    
    private val menuDao = database.menuDao()

    override fun getMenuItems(storeId: Long): Flow<List<MenuItemEntity>> = menuDao.getMenuItems(storeId)

    override suspend fun getMenuItem(id: Long): MenuItemEntity? = menuDao.getMenuItem(id)

    override suspend fun addMenuItem(item: MenuItemEntity) {
        menuDao.insertMenuItem(item)
    }

    override suspend fun updateMenuItem(item: MenuItemEntity) {
        menuDao.updateMenuItem(item)
    }

    override suspend fun deleteMenuItem(id: Long) {
        // 先に関連するアレルゲン紐付けを明示的に削除（外部キーCASCADEが効かない環境への備え）
        database.masterMenuItemAllergenDao().deleteAllForMenu(id)
        menuDao.deleteMenuItemById(id)
    }
    
    // 公開API用メソッド
    override suspend fun getPublicMenuForStore(storeId: Long): List<MenuItemEntity> {
        // 1回分だけ取得して完了させる（無限に collect してキャンセルされないようにする）
        val allItems = menuDao.getMenuItems(storeId).first()
        return allItems.filter { it.isAvailable }
    }
    
    override suspend fun getAllergensForItem(menuItemId: Long): List<String> {
        return database.masterMenuItemAllergenDao()
            .getAllergensForMenu(menuItemId)
            .map { it.allergenCode }
    }

    override suspend fun getAllergens(): List<AllergenEntity> {
        return database.masterAllergenDao().getAllAllergens().map { 
            AllergenEntity(it.code, it.labelJa, it.sortOrder) 
        }
    }

    override suspend fun getMenuItemAllergens(menuItemId: Long): List<MenuItemAllergenEntity> {
        return database.masterMenuItemAllergenDao().getAllergensForMenu(menuItemId).map {
            MenuItemAllergenEntity(
                menuItemId = it.menuItemId,
                allergenCode = it.allergenCode,
                source = it.source
            )
        }
    }

    override suspend fun updateMenuItemAllergens(menuItemId: Long, allergens: List<MenuItemAllergenEntity>) {
        database.masterMenuItemAllergenDao().deleteAllForMenu(menuItemId)
        val masterAllergens = allergens.map {
            MasterMenuItemAllergen(
                menuItemId = it.menuItemId,
                allergenCode = it.allergenCode,
                source = it.source
            )
        }
        database.masterMenuItemAllergenDao().insertAll(masterAllergens)
    }

    override suspend fun initializeAllergensIfNeeded() {
        val dao = database.masterAllergenDao()
        if (dao.getAllAllergens().isEmpty()) {
            val initialAllergens = listOf(
                MasterAllergenEntity("egg", "卵", 1),
                MasterAllergenEntity("milk", "乳", 2),
                MasterAllergenEntity("wheat", "小麦", 3),
                MasterAllergenEntity("buckwheat", "そば", 4),
                MasterAllergenEntity("peanut", "落花生", 5),
                MasterAllergenEntity("shrimp", "えび", 6),
                MasterAllergenEntity("crab", "かに", 7),
                MasterAllergenEntity("walnut", "くるみ", 8),
                MasterAllergenEntity("salmon", "さけ", 9),
                MasterAllergenEntity("mackerel", "さば", 10),
                MasterAllergenEntity("soybean", "大豆", 11),
                MasterAllergenEntity("chicken", "鶏肉", 12),
                MasterAllergenEntity("pork", "豚肉", 13),
                MasterAllergenEntity("gelatin", "ゼラチン", 14),
                MasterAllergenEntity("sesame", "ごま", 15)
            )
            dao.insertAll(initialAllergens)
        }
    }

    override suspend fun enhanceMenuImage(file: java.io.File): java.io.File? {
        try {
            val reqFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, reqFile)
            
            val response = imageEnhancementApiService.enhanceFoodImage(body)
            if (response.isSuccessful && response.body() != null) {
                // Save to temp file
                val bytes = response.body()!!.bytes()
                val outputFile = java.io.File(context.cacheDir, "enhanced_${System.currentTimeMillis()}.jpg")
                outputFile.writeBytes(bytes)
                return outputFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
