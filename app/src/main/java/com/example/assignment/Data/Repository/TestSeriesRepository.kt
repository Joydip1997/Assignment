package com.example.assignment.Data.Repository

import androidx.paging.Pager
import com.example.assignment.Data.Model.DeleteScoreResponse
import com.example.assignment.Data.Model.TestListResponse
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.Data.Model.TestScoreModel.AddTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.EditTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.TestScoreResponse
import com.example.assignment.Data.Model.TestSeriesModel.TestSeriesResponse
import com.example.assignment.Utils.Resource

interface TestSeriesRepository {

    suspend fun getAllTestSeries(email: String, page: Int, limit: Int): Resource<ArrayList<TestListResponse>>

    fun getAllTestSeriesPage(email: String): Pager<Int, TestScore>

    suspend fun getTestScoresById(testId: String): Resource<TestScoreResponse>

    suspend fun getTestSeries(): Resource<TestSeriesResponse>

    suspend fun editTestScoreById(testId: String,editTestScoreDto: EditTestScoreDto): Resource<TestScoreResponse>

    suspend fun addTestScore(addTestScoreDto: AddTestScoreDto): Resource<TestScoreResponse>
    suspend fun deleteTestScoreById(testId: String): Resource<DeleteScoreResponse>

}