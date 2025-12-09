package com.example.restomaster.domain.usecase

import android.util.Log
import com.example.restomaster.data.local.dao.MenuDao
import com.example.restomaster.data.local.entity.MenuTranslationEntity
import com.example.restomaster.data.remote.GeminiTranslationClient
import com.example.restomaster.data.repository.AppSettingsRepository
import com.example.restomaster.data.repository.TranslationRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GenerateTranslationUseCase @Inject constructor(
    private val menuDao: MenuDao,
    private val translationRepository: TranslationRepository,
    private val geminiClient: GeminiTranslationClient,
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend fun execute(menuId: Long, targetLangs: List<String>): Boolean {
        return try {
            val menuItem = menuDao.getMenuItem(menuId) ?: return false
            val apiKey = appSettingsRepository.geminiApiKeyFlow.firstOrNull().orEmpty()
            
            if (apiKey.isBlank()) {
                Log.e("GEN_TRANS", "API Key is missing")
                return false
            }

            val results = geminiClient.translateMenuItem(menuItem, targetLangs, apiKey)
            
            if (results.isEmpty()) return false

            results.forEach { (lang, dto) ->
                val existing = translationRepository.getTranslation(menuId, lang)
                val entity = MenuTranslationEntity(
                    id = existing?.id ?: 0,
                    menuId = menuId,
                    lang = lang,
                    name = dto.name,
                    description = dto.description,
                    status = "draft",
                    updatedAt = System.currentTimeMillis()
                )
                translationRepository.saveTranslation(entity)
            }
            
            true
        } catch (e: Exception) {
            Log.e("GEN_TRANS", "Generation failed: ${e.message}", e)
            false
        }
    }
}
