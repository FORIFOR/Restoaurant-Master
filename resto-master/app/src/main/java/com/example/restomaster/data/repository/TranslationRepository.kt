package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.MenuTranslationDao
import com.example.restomaster.data.local.entity.MenuTranslationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationRepository @Inject constructor(
    private val translationDao: MenuTranslationDao
) {
    fun getTranslationsForMenu(menuId: Long): Flow<List<MenuTranslationEntity>> {
        return translationDao.getTranslationsForMenu(menuId)
    }

    suspend fun getTranslation(menuId: Long, lang: String): MenuTranslationEntity? {
        return translationDao.getTranslation(menuId, lang)
    }

    suspend fun getApprovedTranslationsForStore(storeId: Long, lang: String): List<MenuTranslationEntity> {
        return translationDao.getApprovedTranslationsForStore(storeId, lang)
    }

    suspend fun saveTranslation(translation: MenuTranslationEntity) {
        translationDao.insertOrUpdate(translation)
    }

    suspend fun deleteTranslation(id: Long) {
        translationDao.deleteById(id)
    }
}
