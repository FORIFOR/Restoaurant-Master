package com.example.restomaster.domain.usecase

import com.example.restomaster.domain.repository.MenuRepository
import javax.inject.Inject

class BatchTranslateMenusUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val translationRepository: com.example.restomaster.data.repository.TranslationRepository,
    private val generateTranslationUseCase: GenerateTranslationUseCase
) {
    /**
     * Translates all menu items for a given store to the target language.
     * @param storeId The ID of the store
     * @param targetLanguage The target language code (e.g., "en", "zh-Hans", "fr", "it")
     * @param forceUpdate If true, re-translates even if translation exists
     * @param onProgress Callback invoked after each item is processed (current, total)
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(
        storeId: Long,
        targetLanguage: String,
        forceUpdate: Boolean = false,
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> }
    ): Result<Unit> {
        return try {
            val menuItems = menuRepository.getPublicMenuForStore(storeId)
            val total = menuItems.size
            
            menuItems.forEachIndexed { index, menuItem ->
                // Check if translation already exists
                val existingTranslation = translationRepository.getTranslation(menuItem.id, targetLanguage)
                
                if (forceUpdate || existingTranslation == null) {
                    // Generate translation if forced or not exists
                    generateTranslationUseCase.execute(
                        menuId = menuItem.id,
                        targetLangs = listOf(targetLanguage)
                    )
                }
                
                // Report progress
                onProgress(index + 1, total)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
