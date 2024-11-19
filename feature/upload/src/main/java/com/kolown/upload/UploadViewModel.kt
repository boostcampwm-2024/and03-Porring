package com.kolown.upload

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(

) : ViewModel() {
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _categoryItems = MutableStateFlow<List<String>>(emptyList())
    val categoryItems = _categoryItems.asStateFlow()

    fun changeDescription(description: String) {
        _description.value = description
    }

    fun addCategory() {
        _categoryItems.value += ""
    }

    fun changeCategoryName(index: Int, name: String) {
        val newList = _categoryItems.value.toMutableList()
        newList[index] = name
        _categoryItems.value = newList
    }

    fun removeCategory(category: String) {
        _categoryItems.value -= category
    }

}