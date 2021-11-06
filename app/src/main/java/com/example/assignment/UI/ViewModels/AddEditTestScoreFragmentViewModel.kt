package com.example.assignment.UI.ViewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.Data.Model.TestScoreModel.AddTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.EditTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.Scores
import com.example.assignment.Data.Repository.TestSeriesRepository
import com.example.assignment.Utils.CommonUtils
import com.example.assignment.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTestScoreFragmentViewModel @Inject constructor(
    private val testSeriesRepository: TestSeriesRepository
) : ViewModel(), DefaultLifecycleObserver {

    sealed class TestScoreState {
        data class TestScoresAvailable(val testScore: TestScore) : TestScoreState()
        object Error : TestScoreState()
        object SZero : TestScoreState()
    }

    sealed class TestSeriesState {
        data class TestSeriesStateAvailable(val testSeries: List<String>) : TestSeriesState()
        object Error : TestSeriesState()
        object SZero : TestSeriesState()
    }


    private var testSeriesName = ""
    private var testName = ""
    private var testDate = ""
    private var physicsScore = 0
    private var chemScore = 0
    private var mathsScore = 0

    private val _loadingEvent =
        MutableSharedFlow<Boolean>()
    val loadingEvent = _loadingEvent


    private val _addEditTestEvent =
        MutableSharedFlow<Boolean>()
    val addEditTestEvent = _addEditTestEvent


    private val _testScoreState =
        MutableStateFlow<TestScoreState>(TestScoreState.SZero)
    val testScoreState = _testScoreState

    private val _testSeriesState =
        MutableStateFlow<TestSeriesState>(TestSeriesState.SZero)
    val testSeriesState = _testSeriesState

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTestScoresById(testId: String) {
        viewModelScope.launch {
            when (val response = testSeriesRepository.getTestScoresById(testId)) {
                is Resource.Success -> {
                    val dateObj =
                        CommonUtils.convertTimeFromString(response.data!!.testScore.testDate)
                    val year = dateObj.year.toString()
                    val month = dateObj.monthValue
                    val date = CommonUtils.getFormattedDay(dateObj.dayOfMonth)
                    val formattedDate = "$month/$date/$year"
                    response.data.testScore.formatDate(formattedDate)
                    _testScoreState.emit(TestScoreState.TestScoresAvailable(response.data.testScore))
                }
                is Resource.Error -> {
                    _testScoreState.emit(TestScoreState.Error)
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fetchTestSeries()
    }

    private fun fetchTestSeries() {
        viewModelScope.launch {
            _loadingEvent.emit(true)
            when (val response = testSeriesRepository.getTestSeries()) {
                is Resource.Success -> {
                    delay(2000)
                    _loadingEvent.emit(false)

                    _testSeriesState.emit(TestSeriesState.TestSeriesStateAvailable(response.data!!.testSeries))
                }
                is Resource.Error -> {
                    _loadingEvent.emit(false)
                    _testSeriesState.emit(TestSeriesState.Error)
                }
            }
        }
    }

    fun addNewTestScore() {
        val addTestScoreDto = AddTestScoreDto(
            "noneuser2183@gmail.com", "Jee Mains", Scores(
                chemScore, mathsScore, physicsScore
            ), testDate, testName, testSeriesName
        )
        viewModelScope.launch {
            _loadingEvent.emit(true)
            when (val response = testSeriesRepository.addTestScore(addTestScoreDto)) {
                is Resource.Success -> {
                    _addEditTestEvent.emit(true)
                    delay(100)
                    _loadingEvent.emit(false)
                    _testScoreState.emit(TestScoreState.TestScoresAvailable(response.data!!.testScore))
                }
                is Resource.Error -> {
                    _testScoreState.emit(TestScoreState.Error)
                    _loadingEvent.emit(false)
                    _addEditTestEvent.emit(false)
                }
            }
        }
    }


    fun editTestScore(testId: String, editTestScoreDto: EditTestScoreDto) {
        viewModelScope.launch {
            _loadingEvent.emit(true)
            when (val response = testSeriesRepository.editTestScoreById(testId, editTestScoreDto)) {
                is Resource.Success -> {
                    _addEditTestEvent.emit(true)
                    delay(100)
                    _loadingEvent.emit(false)
                    _testScoreState.emit(TestScoreState.TestScoresAvailable(response.data!!.testScore))
                }
                is Resource.Error -> {
                    _testScoreState.emit(TestScoreState.Error)
                    _loadingEvent.emit(false)
                    _addEditTestEvent.emit(false)
                }
            }
        }
    }


    fun selectTestSeries(testSeriesName: String) {
        this.testSeriesName = testSeriesName
    }

    fun addEditTestName(testName: String) {
        this.testName = testName
    }

    fun addEditTestDate(testDate: String) {
        this.testDate = testDate
    }

    fun addEditTestScores(physicsScore: String, chemScore: String, mathsScore: String): Boolean {
        when {
            physicsScore.isEmpty() || physicsScore.toInt() < 0 || physicsScore.toInt() > 100 -> {
                return false
            }
            chemScore.isEmpty() || chemScore.toInt() < 0 || chemScore.toInt() > 100 -> {
                return false
            }
            mathsScore.isEmpty() || mathsScore.toInt() < 0 || mathsScore.toInt() > 100 -> {
                return false
            }
        }
        this.physicsScore = physicsScore.toInt()
        this.chemScore = chemScore.toInt()
        this.mathsScore = mathsScore.toInt()
        return true

    }


}