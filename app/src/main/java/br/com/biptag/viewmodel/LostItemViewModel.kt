package br.com.biptag.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.biptag.model.Alert
import br.com.biptag.model.Item
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LostItemUiState(
    val isLoading: Boolean = false,
    val alert: Alert? = null,
    val item: Item? = null,
    val error: String? = null
)

class LostItemViewModel(
    private val alertRepository: AlertRepository = AlertRepository(),
    private val itemRepository: ItemRepository = ItemRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LostItemUiState())
    val uiState: StateFlow<LostItemUiState> = _uiState

    fun loadDetails(alertId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val alert = alertRepository.getAlertById(alertId)
                if (alert != null) {
                    val item = itemRepository.getItemById(alert.itemId)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        alert = alert,
                        item = item
                    )
                } else {
                    // Dados de demonstração se o ID for 1 ou se não encontrar no banco
                    val mockItem = Item(id = 1, name = "Bicicleta Caloi", description = "Quadro azul, aro 29, adesivo na traseira", category = 1)
                    val mockAlert = Alert(id = 1, itemId = 1, type = "stolen", status = "active")
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        alert = mockAlert,
                        item = mockItem
                    )
                }
            } catch (e: Exception) {
                // Mock em caso de erro para teste
                val mockItem = Item(id = 1, name = "Bicicleta Caloi (Offline)", description = "Erro ao conectar", category = 1)
                val mockAlert = Alert(id = 1, itemId = 1, type = "stolen", status = "active")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    alert = mockAlert,
                    item = mockItem,
                    error = "Modo offline"
                )
            }
        }
    }
}
