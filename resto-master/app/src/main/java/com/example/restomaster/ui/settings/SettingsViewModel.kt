package com.example.restomaster.ui.settings

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.repository.AppSettingsRepository
import com.example.restomaster.domain.model.AppMode
import com.example.restomaster.service.HttpServerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appSettingsRepository: AppSettingsRepository,
    private val billingRepository: com.example.restomaster.data.repository.BillingRepository,
    private val storeDao: com.example.restomaster.data.local.dao.StoreDao
) : ViewModel() {

    data class SettingsUiState(
        val mode: AppMode = AppMode.HOST,
        val hostIpInput: String = "",
        val hostPortInput: String = "8080",
        val isTestingConnection: Boolean = false,
        val connectionStatus: ConnectionStatus = ConnectionStatus.Unknown,
        val isServerRunning: Boolean = false,  // サーバ起動状態
        val geminiApiKeyInput: String = "",
        val geminiKeyMasked: String = "未設定",
        val geminiSaveMessage: String? = null,
        val openAiApiKeyInput: String = "",
        val openAiKeyMasked: String = "未設定",
        val openAiSaveMessage: String? = null,
        // Store settings
        val storeName: String = "",
        val storeInvoiceNumber: String = "",
        val storePostal: String = "",
        val storeAddress1: String = "",
        val storeAddress2: String = "",
        val storePhone: String = "",
        val storeTaxIncluded: Boolean = true,
        val storeSaveMessage: String? = null,
        val storeErrorMessage: String? = null,
        // Customer web app customization
        val splashImagePath: String? = null,
        val themeColor: String = "#D35400"  // Default orange
    )

    enum class ConnectionStatus {
        Unknown, Success, Failure
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            appSettingsRepository.settingsFlow.collect { settings ->
                _uiState.update {
                    it.copy(
                        mode = settings.mode,
                        hostIpInput = settings.hostIp,
                        hostPortInput = settings.hostPort.toString()
                    )
                }
            }
        }
        viewModelScope.launch {
            appSettingsRepository.geminiApiKeyFlow.collect { key ->
                _uiState.update {
                    it.copy(
                        geminiApiKeyInput = key.orEmpty(),
                        geminiKeyMasked = if (key.isNullOrBlank()) "未設定" else "********",
                        geminiSaveMessage = null
                    )
                }
            }
        }
        viewModelScope.launch {
            appSettingsRepository.openAiApiKeyFlow.collect { key ->
                _uiState.update {
                    it.copy(
                        openAiApiKeyInput = key.orEmpty(),
                        openAiKeyMasked = if (key.isNullOrBlank()) "未設定" else "********",
                        openAiSaveMessage = null
                    )
                }
            }
        }
        viewModelScope.launch {
            storeDao.getStoreFlow().collect { store ->
                _uiState.update {
                    it.copy(
                        storeName = store?.name.orEmpty(),
                        storeInvoiceNumber = store?.invoiceRegistrationNumber.orEmpty(),
                        storePostal = store?.postalCode.orEmpty(),
                        storeAddress1 = store?.addressLine1 ?: store?.address ?: "",
                        storeAddress2 = store?.addressLine2.orEmpty(),
                        storePhone = store?.phone.orEmpty(),
                        storeTaxIncluded = store?.taxIncludedPricing ?: true,
                        splashImagePath = store?.splashImagePath,
                        themeColor = store?.themeColor ?: "#D35400",
                        storeSaveMessage = null
                    )
                }
            }
        }
    }

    fun onModeChange(mode: AppMode) {
        viewModelScope.launch {
            appSettingsRepository.updateMode(mode)
        }
    }

    fun onHostIpChange(value: String) {
        _uiState.update { it.copy(hostIpInput = value) }
    }

    fun onHostPortChange(value: String) {
        _uiState.update { it.copy(hostPortInput = value.filter { c -> c.isDigit() }) }
    }

    fun onSaveHostInfo() {
        val state = _uiState.value
        val port = state.hostPortInput.toIntOrNull() ?: return
        viewModelScope.launch {
            appSettingsRepository.updateHostInfo(state.hostIpInput, port)
        }
    }

    fun onTestConnection() {
        val state = _uiState.value
        val ip = state.hostIpInput
        val port = state.hostPortInput.toIntOrNull() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isTestingConnection = true, connectionStatus = ConnectionStatus.Unknown) }
            try {
                // Simple HTTP GET to /health endpoint
                val url = URL("http://$ip:$port/health")
                (url.openConnection() as HttpURLConnection).run {
                    connectTimeout = 2000
                    readTimeout = 2000
                    requestMethod = "GET"
                    connect()
                    val success = responseCode in 200..299
                    disconnect()
                    _uiState.update {
                        it.copy(
                            isTestingConnection = false,
                            connectionStatus = if (success) ConnectionStatus.Success else ConnectionStatus.Failure
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isTestingConnection = false,
                        connectionStatus = ConnectionStatus.Failure
                    )
                }
            }
        }
    }
    
    /**
     * HTTPサーバを起動
     */
    fun startServer() {
        val intent = Intent(context, HttpServerService::class.java).apply {
            action = HttpServerService.ACTION_START
        }
        context.startForegroundService(intent)
        _uiState.update { it.copy(isServerRunning = true) }
    }
    
    /**
     * HTTPサーバを停止
     */
    fun stopServer() {
        val intent = Intent(context, HttpServerService::class.java).apply {
            action = HttpServerService.ACTION_STOP
        }
        context.startService(intent)
        _uiState.update { it.copy(isServerRunning = false) }
    }
    
    /**
     * サーバON/OFF切り替え
     */
    fun toggleServer(enabled: Boolean) {
        if (enabled) {
            startServer()
        } else {
            stopServer()
        }
    }

    fun onGeminiApiKeyChange(value: String) {
        _uiState.update { it.copy(geminiApiKeyInput = value) }
    }

    fun onSaveGeminiApiKey() {
        val key = _uiState.value.geminiApiKeyInput.trim()
        viewModelScope.launch {
            appSettingsRepository.updateGeminiApiKey(if (key.isBlank()) null else key)
            _uiState.update { it.copy(geminiSaveMessage = if (key.isBlank()) "削除しました" else "保存しました") }
        }
    }

    fun onOpenAiApiKeyChange(value: String) {
        _uiState.update { it.copy(openAiApiKeyInput = value) }
    }

    fun onSaveOpenAiApiKey() {
        val key = _uiState.value.openAiApiKeyInput.trim()
        viewModelScope.launch {
            appSettingsRepository.updateOpenAiApiKey(if (key.isBlank()) null else key)
            _uiState.update { it.copy(openAiSaveMessage = if (key.isBlank()) "削除しました" else "保存しました") }
        }
    }

    // Store settings
    fun onStoreNameChange(value: String) { _uiState.update { it.copy(storeName = value) } }
    fun onStoreInvoiceNumberChange(value: String) { _uiState.update { it.copy(storeInvoiceNumber = value) } }
    fun onStorePostalChange(value: String) { _uiState.update { it.copy(storePostal = value) } }
    fun onStoreAddress1Change(value: String) { _uiState.update { it.copy(storeAddress1 = value) } }
    fun onStoreAddress2Change(value: String) { _uiState.update { it.copy(storeAddress2 = value) } }
    fun onStorePhoneChange(value: String) { _uiState.update { it.copy(storePhone = value) } }
    fun onStoreTaxIncludedChange(value: Boolean) { _uiState.update { it.copy(storeTaxIncluded = value) } }
    fun onSplashImagePathChange(value: String?) { _uiState.update { it.copy(splashImagePath = value) } }
    fun onThemeColorChange(value: String) { _uiState.update { it.copy(themeColor = value) } }
    
    /**
     * Handle splash image selection from gallery.
     * Copies the selected image to app's internal storage and updates the path.
     */
    fun onSplashImageSelected(context: android.content.Context, uri: android.net.Uri) {
        viewModelScope.launch {
            try {
                // Create splash images directory
                val splashDir = java.io.File(context.filesDir, "splash")
                if (!splashDir.exists()) splashDir.mkdirs()
                
                // Generate unique filename
                val filename = "splash_${System.currentTimeMillis()}.jpg"
                val destFile = java.io.File(splashDir, filename)
                
                // Copy image from content URI to internal storage
                context.contentResolver.openInputStream(uri)?.use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Update state with new path
                _uiState.update { it.copy(splashImagePath = destFile.absolutePath) }
            } catch (e: Exception) {
                android.util.Log.e("SettingsViewModel", "Failed to save splash image", e)
            }
        }
    }

    fun onSaveStore() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.storeInvoiceNumber.isBlank()) {
                _uiState.update { it.copy(storeErrorMessage = "登録番号(T〜)を入力してください") }
                return@launch
            }
            if (state.storeAddress1.isBlank()) {
                _uiState.update { it.copy(storeErrorMessage = "住所を入力してください") }
                return@launch
            }
            val store = com.example.restomaster.data.local.entity.StoreEntity(
                id = 1L, // シングル店舗運用前提
                name = state.storeName.ifBlank { "店舗名未設定" },
                address = state.storeAddress1.ifBlank { null },
                phone = state.storePhone.ifBlank { null },
                invoiceRegistrationNumber = state.storeInvoiceNumber.ifBlank { null },
                postalCode = state.storePostal.ifBlank { null },
                addressLine1 = state.storeAddress1.ifBlank { null },
                addressLine2 = state.storeAddress2.ifBlank { null },
                taxIncludedPricing = state.storeTaxIncluded,
                splashImagePath = state.splashImagePath,
                themeColor = state.themeColor.ifBlank { null }
            )
            storeDao.insertStore(store)
            _uiState.update { it.copy(storeSaveMessage = "店舗情報を保存しました", storeErrorMessage = null) }
        }
    }

    // Legacy compatibility
    val isHostMode: Boolean
        get() = _uiState.value.mode == AppMode.HOST

    fun toggleMode(isHost: Boolean) {
        onModeChange(if (isHost) AppMode.HOST else AppMode.CLIENT)
    }
}
