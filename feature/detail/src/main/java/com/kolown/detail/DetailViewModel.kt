package com.kolown.detail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.ImageRepository
import com.kolown.model.ImageItem
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @FakeImageRepository
    private val imageRepository  : ImageRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ImageItem>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _items =  emptyList<ImageItem>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun getItem(currentPage: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                imageRepository.getItemByPage(currentPage)
                    .catch { e ->
                        _uiState.value = UiState.Failure(e)
                        Log.e("익셉션1",e.message.toString())
                        delay(Duration.ofSeconds(4))
                        _uiState.value = UiState.Success(_items+_items+_items+_items)
                        _items = _items+_items+_items
                    }
                    .collect { items ->
                        val currentItems = (_uiState.value as? UiState.Success<List<ImageItem>>)?.data ?: emptyList()
                        val updatedItems = currentItems + items
                        _uiState.value = UiState.Success(updatedItems)
                        _items = updatedItems
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e)
                Log.e("익셉션2",e.message.toString())
            }
        }
    }

    fun getPagingItem() : List<ImageItem> = _items
}