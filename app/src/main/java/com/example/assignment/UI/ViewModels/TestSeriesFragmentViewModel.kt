package com.example.assignment.UI.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.Data.Repository.TestSeriesRepository
import com.example.assignment.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestSeriesFragmentViewModel @Inject constructor(
    private val testSeriesRepository: TestSeriesRepository
) : ViewModel() {



    private var email = ""
    private var currentSearchResult: Flow<PagingData<TestScore>>? = null

    private val _loadingEvent =
        MutableSharedFlow<Boolean>()
    val loadingEvent = _loadingEvent

    private val _deleteScoreEvent =
        MutableSharedFlow<Boolean>()
    val deleteScoreEvent = _deleteScoreEvent





    fun fetchTestSeries(email: String): Flow<PagingData<TestScore>> {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return lastResult
        }
        this.email = email

        val data = testSeriesRepository.getAllTestSeriesPage(email).flow

        val newResult: Flow<PagingData<TestScore>> = data

        currentSearchResult = newResult
        return newResult
    }

    fun deleteTestScoreById(testId: String) {
        viewModelScope.launch {
            _loadingEvent.emit(true)
            when (testSeriesRepository.deleteTestScoreById(testId)) {
                is Resource.Success -> {
                    _deleteScoreEvent.emit(true)
                }
                is Resource.Error -> {
                    _deleteScoreEvent.emit(false)
                    _loadingEvent.emit(false)
                }
            }
        }
    }


}