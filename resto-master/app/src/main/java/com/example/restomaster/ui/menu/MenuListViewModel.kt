package com.example.restomaster.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.domain.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.restomaster.data.repository.TranslationRepository
import com.example.restomaster.domain.usecase.BatchTranslateMenusUseCase

@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val translationRepository: TranslationRepository,
    private val batchTranslateMenusUseCase: BatchTranslateMenusUseCase
) : ViewModel() {

    private val storeId = 1L

    private val _selectedCategory = MutableStateFlow("Food")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage: StateFlow<String?> = _selectedLanguage

    private val _translationInProgress = MutableStateFlow(false)
    private val _translationProgress = MutableStateFlow(0 to 0) // current to total
    private val _translationError = MutableStateFlow<String?>(null)

    private val _menuItems = menuRepository.getMenuItems(storeId)

    val uiState: StateFlow<MenuUiState> = combine(
        _menuItems,
        _selectedCategory,
        _selectedLanguage,
        _translationInProgress,
        _translationProgress,
        _translationError
    ) { array ->
        @Suppress("UNCHECKED_CAST")
        val items = array[0] as List<MenuItemEntity>
        val category = array[1] as String
        val lang = array[2] as String?
        val inProgress = array[3] as Boolean
        val progress = array[4] as Pair<Int, Int>
        val error = array[5] as String?
        
        val filteredItems = items.filter { it.category.equals(category, ignoreCase = true) }
        
        val displayItems = if (lang == null) {
            filteredItems.map { DisplayMenuItem(it, it.name, it.description) }
        } else {
            // Load translations for all items if a language is selected
            // Note: In a real app, we might want to optimize this to not query in a loop or use a join
            // For now, we'll fetch translations for the filtered items
            filteredItems.map { item ->
                val translation = translationRepository.getTranslation(item.id, lang)
                DisplayMenuItem(
                    originalItem = item,
                    displayName = translation?.name ?: item.name,
                    displayDescription = translation?.description ?: item.description
                )
            }
        }

        val translationCompletionRate = if (lang == null || items.isEmpty()) {
            0f
        } else {
            // Calculate completion rate for ALL items (not just filtered) to show global status
            // This is a bit heavy for the UI thread, ideally should be done in a background flow
            // For now, we'll approximate using the displayItems logic which is already running
            val translatedCount = displayItems.count { it.displayName != it.originalItem.name }
            if (displayItems.isEmpty()) 0f else translatedCount.toFloat() / displayItems.size
        }

        MenuUiState(
            displayItems = displayItems,
            selectedCategory = category,
            selectedLanguage = lang,
            translationInProgress = inProgress,
            translationProgress = progress,
            translationError = error,
            translationCompletionRate = translationCompletionRate
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MenuUiState()
    )

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectLanguage(lang: String?) {
        _selectedLanguage.value = lang
    }

    fun translateAllMenus(targetLang: String, forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _translationInProgress.value = true
            _translationError.value = null
            _translationProgress.value = 0 to 0
            
            val result = batchTranslateMenusUseCase(
                storeId = storeId,
                targetLanguage = targetLang,
                forceUpdate = forceUpdate,
                onProgress = { current, total ->
                    _translationProgress.value = current to total
                }
            )
            
            _translationInProgress.value = false
            
            result.onFailure { error ->
                _translationError.value = error.message ?: "翻訳に失敗しました"
            }
        }
    }

    fun clearTranslationError() {
        _translationError.value = null
    }

    fun addMenuItem(name: String, price: Int, category: String, description: String?, imagePath: String?) {
        viewModelScope.launch {
            val item = MenuItemEntity(
                storeId = storeId,
                name = name,
                price = price,
                category = category,
                description = description,
                imagePath = imagePath
            )
            menuRepository.addMenuItem(item)
        }
    }

    fun deleteMenuItem(id: Long) {
        viewModelScope.launch {
            menuRepository.deleteMenuItem(id)
        }
    }

    fun toggleAvailability(item: MenuItemEntity) {
        viewModelScope.launch {
            menuRepository.updateMenuItem(item.copy(isAvailable = !item.isAvailable))
        }
    }
}

data class DisplayMenuItem(
    val originalItem: MenuItemEntity,
    val displayName: String,
    val displayDescription: String?
)

data class MenuUiState(
    val displayItems: List<DisplayMenuItem> = emptyList(),
    val selectedCategory: String = "Food",
    val selectedLanguage: String? = null,
    val translationInProgress: Boolean = false,
    val translationProgress: Pair<Int, Int> = 0 to 0,
    val translationError: String? = null,
    val translationCompletionRate: Float = 0f
)
