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

data class MapUiState(
    val isLoading: Boolean = false,
    val alerts: List<Alert> = emptyList(),
    val itemsMap: Map<Int, Item> = emptyMap(),
    val selectedAlert: Alert? = null,
    val error: String? = null
)

class MapViewModel(
    private val alertRepository: AlertRepository = AlertRepository(),
    private val itemRepository: ItemRepository = ItemRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val activeAlerts = alertRepository.getActiveAlerts()
                val items = mutableMapOf<Int, Item>()
                
                activeAlerts.forEach { alert ->
                    itemRepository.getItemById(alert.itemId)?.let { item ->
                        items[alert.itemId] = item
                    }
                }

                // Se não houver alertas no banco, adiciona mocks para teste visual
                val finalAlerts = if (activeAlerts.isEmpty()) {
                    val mockItems = listOf(
                        Item(id = 1, name = "Bicicleta Caloi", description = "Quadro azul, aro 29", category = 1),
                        Item(id = 2, name = "Mochila Dell", description = "Preta com detalhes vermelhos", category = 2)
                    )
                    mockItems.forEach { items[it.id!!] = it }
                    listOf(
                        Alert(id = 101, itemId = 1, type = "stolen", latitude = -23.56, longitude = -46.65, status = "active"),
                        Alert(id = 102, itemId = 2, type = "lost", latitude = -23.57, longitude = -46.66, status = "active")
                    )
                } else {
                    activeAlerts
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    alerts = finalAlerts,
                    itemsMap = items,
                    selectedAlert = finalAlerts.firstOrNull()
                )
            } catch (e: Exception) {
                // Em caso de erro, também podemos mostrar mocks para não travar o desenvolvimento
                val mockItem = Item(id = 1, name = "Bicicleta Caloi (Offline)", description = "Modo de demonstração", category = 1)
                val mockAlert = Alert(id = 101, itemId = 1, type = "stolen", status = "active")
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    alerts = listOf(mockAlert),
                    itemsMap = mapOf(1 to mockItem),
                    selectedAlert = mockAlert,
                    error = "Servidor offline. Exibindo dados de teste."
                )
            }
        }
    }

    fun selectAlert(alert: Alert) {
        _uiState.value = _uiState.value.copy(selectedAlert = alert)
    }
}
