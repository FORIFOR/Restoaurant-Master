package com.example.restomaster.ui.menu.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.data.local.entity.MenuItemAllergenEntity
import com.example.restomaster.domain.ai.AllergenSuggestionEngine
import com.example.restomaster.domain.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.restomaster.data.local.dao.AllergenFeedbackDao
import com.example.restomaster.data.local.entity.AllergenFeedbackEntity
import com.example.restomaster.data.local.entity.MenuTranslationEntity
import com.example.restomaster.data.repository.TranslationRepository
import com.example.restomaster.domain.usecase.GenerateTranslationUseCase

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val translationRepository: TranslationRepository,
    private val generateTranslationUseCase: GenerateTranslationUseCase,
    private val generateMenuImageUseCase: com.example.restomaster.domain.usecase.GenerateMenuImageUseCase,
    private val allergenSuggestionEngine: AllergenSuggestionEngine,
    private val allergenFeedbackDao: AllergenFeedbackDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val menuItemId: Long = checkNotNull(savedStateHandle["menuItemId"])

    data class AllergenUiItem(
        val allergenCode: String,
        val label: String,
        val checked: Boolean,
        val suggestedByAi: Boolean,
        val confidence: Float?
    )

    data class DetailUiState(
        val menuItem: MenuItemEntity? = null,
        val allergens: List<AllergenUiItem> = emptyList(),
        val translations: List<MenuTranslationEntity> = emptyList(),
        val isSuggesting: Boolean = false,
        val isTranslating: Boolean = false,
        val isGeneratingImage: Boolean = false,
        val isEnhancing: Boolean = false,
        val enhancedImagePath: String? = null,
        val isSaving: Boolean = false,
        val selectedTranslationLanguages: Set<String> = setOf("en", "zh-Hans", "fr", "it")
    )

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Ensure master allergens exist
            menuRepository.initializeAllergensIfNeeded()
            
            val item = menuRepository.getMenuItem(menuItemId)
            val allAllergens = menuRepository.getAllergens()
            val existingRelations = menuRepository.getMenuItemAllergens(menuItemId)

            val uiAllergens = allAllergens.map { allergen ->
                val relation = existingRelations.find { it.allergenCode == allergen.code }
                AllergenUiItem(
                    allergenCode = allergen.code,
                    label = allergen.labelJa,
                    checked = relation != null,
                    suggestedByAi = relation?.source == "ai",
                    confidence = null // Loaded from DB, no confidence stored
                )
            }

            _uiState.update {
                it.copy(
                    menuItem = item,
                    allergens = uiAllergens
                )
            }
            
            // Load translations
            translationRepository.getTranslationsForMenu(menuItemId).collect { translations ->
                _uiState.update { it.copy(translations = translations) }
            }
        }
    }

    fun onAllergenToggle(code: String) {
        _uiState.update { state ->
            val updated = state.allergens.map {
                if (it.allergenCode == code) {
                    it.copy(checked = !it.checked)
                } else {
                    it
                }
            }
            state.copy(allergens = updated)
        }
    }

    fun onTranslationLanguageToggle(lang: String) {
        _uiState.update { state ->
            val current = state.selectedTranslationLanguages
            val newSet = if (current.contains(lang)) {
                current - lang
            } else {
                current + lang
            }
            state.copy(selectedTranslationLanguages = newSet)
        }
    }

    fun onSuggestAllergens() {
        val item = _uiState.value.menuItem ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSuggesting = true) }
            
            val suggestions = allergenSuggestionEngine.suggest(item.name, item.description)
            val threshold = 0.6f

            _uiState.update { state ->
                val updated = state.allergens.map { uiItem ->
                    val s = suggestions.find { it.allergenCode == uiItem.allergenCode }
                    if (s != null && s.confidence >= threshold) {
                        uiItem.copy(
                            checked = true,
                            suggestedByAi = true,
                            confidence = s.confidence
                        )
                    } else {
                        uiItem
                    }
                }
                state.copy(allergens = updated, isSuggesting = false)
            }
        }
    }

    fun onGenerateTranslations(langs: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true) }
            generateTranslationUseCase.execute(menuItemId, langs)
            _uiState.update { it.copy(isTranslating = false) }
        }
    }
    
    fun onGenerateAiImage() {
        val item = _uiState.value.menuItem ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingImage = true) }
            val imagePath = generateMenuImageUseCase.execute(item.name)
            if (imagePath != null) {
                onImageUpdate(imagePath, isAiGenerated = true)
            }
            _uiState.update { it.copy(isGeneratingImage = false) }
        }
    }

    fun onEnhanceImage() {
        val item = _uiState.value.menuItem ?: return
        val currentPath = item.imagePath ?: return
        
        if (currentPath.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isEnhancing = true) }
            val file = java.io.File(currentPath)
            if (file.exists()) {
                val enhancedFile = menuRepository.enhanceMenuImage(file)
                if (enhancedFile != null) {
                    _uiState.update { it.copy(enhancedImagePath = enhancedFile.absolutePath) }
                }
            }
            _uiState.update { it.copy(isEnhancing = false) }
        }
    }

    fun onApplyEnhancedImage() {
        val enhancedPath = _uiState.value.enhancedImagePath ?: return
        onImageUpdate(enhancedPath, isAiGenerated = true) // Mark logic as AI-touched
        _uiState.update { it.copy(enhancedImagePath = null) }
    }

    fun onDiscardEnhancedImage() {
        _uiState.update { it.copy(enhancedImagePath = null) }
    }

    fun onUpdateTranslation(translation: MenuTranslationEntity) {
        viewModelScope.launch {
            translationRepository.saveTranslation(translation)
        }
    }

    fun onApproveTranslation(translation: MenuTranslationEntity) {
        viewModelScope.launch {
            val approved = translation.copy(status = "approved", updatedAt = System.currentTimeMillis())
            translationRepository.saveTranslation(approved)
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { state ->
            state.copy(menuItem = state.menuItem?.copy(description = newDescription))
        }
    }

    fun onImageUpdate(newPath: String, isAiGenerated: Boolean = false) {
        _uiState.update { state ->
            state.copy(menuItem = state.menuItem?.copy(imagePath = newPath, isAiGenerated = isAiGenerated))
        }
    }

    fun onSave() {
        val item = _uiState.value.menuItem ?: return
        val currentAllergens = _uiState.value.allergens
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            // Save menu item details (including description)
            menuRepository.updateMenuItem(item)
            
            // Save allergen relations
            val relations = currentAllergens
                .filter { it.checked }
                .map { uiItem ->
                    MenuItemAllergenEntity(
                        menuItemId = item.id,
                        allergenCode = uiItem.allergenCode,
                        source = if (uiItem.suggestedByAi) "ai" else "manual"
                    )
                }
            menuRepository.updateMenuItemAllergens(item.id, relations)

            // Record feedback
            val timestamp = System.currentTimeMillis()
            currentAllergens.forEach { uiItem ->
                val action = when {
                    uiItem.suggestedByAi && uiItem.checked -> "kept"
                    uiItem.suggestedByAi && !uiItem.checked -> "removed"
                    !uiItem.suggestedByAi && uiItem.checked -> "added"
                    else -> null
                }

                if (action != null) {
                    allergenFeedbackDao.insert(
                        AllergenFeedbackEntity(
                            menuItemId = item.id,
                            allergenCode = uiItem.allergenCode,
                            aiSuggested = uiItem.suggestedByAi,
                            userAction = action,
                            timestamp = timestamp
                        )
                    )
                }
            }
            
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}
