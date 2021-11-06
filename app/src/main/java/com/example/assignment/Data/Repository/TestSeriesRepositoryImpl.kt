package com.example.assignment.Data.Repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.assignment.Data.Model.DeleteScoreResponse
import com.example.assignment.Data.Model.TestListResponse
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.Data.Model.TestScoreModel.AddTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.EditTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.TestScoreResponse
import com.example.assignment.Data.Model.TestSeriesModel.TestSeriesResponse
import com.example.assignment.Data.TestScoreListPagingSource
import com.example.assignment.Retrofit.ApiClient
import com.example.assignment.Utils.ApiError
import com.example.assignment.Utils.Resource

class TestSeriesRepositoryImpl : TestSeriesRepository {

    override suspend fun getAllTestSeries(
        email: String,
        page: Int,
        limit: Int
    ): Resource<ArrayList<TestListResponse>> {
        val response = ApiClient.getmRetrofitInstance().getAllTestScores(email, page, limit)

        val list = ArrayList<TestListResponse>()
        list.add(response.body()!!)
        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(list)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }

    }

    override fun getAllTestSeriesPage(email: String): Pager<Int, TestScore> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false

            ),
            pagingSourceFactory = {
                TestScoreListPagingSource(email)
            }
        )

    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }


    override suspend fun getTestScoresById(testId: String): Resource<TestScoreResponse> {
        val response = ApiClient.getmRetrofitInstance().getTestScores(testId)
        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }
    }

    override suspend fun getTestSeries(): Resource<TestSeriesResponse> {
        val response = ApiClient.getmRetrofitInstance().getTestSeries()
        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }
    }

    override suspend fun editTestScoreById(
        testId: String,
        editTestScoreDto: EditTestScoreDto
    ): Resource<TestScoreResponse> {
        val response = ApiClient.getmRetrofitInstance().editTestScoreById(testId, editTestScoreDto)
        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }
    }

    override suspend fun addTestScore(addTestScoreDto: AddTestScoreDto): Resource<TestScoreResponse> {
        val response = ApiClient.getmRetrofitInstance().addTestScore(addTestScoreDto)

        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }
    }

    override suspend fun deleteTestScoreById(testId: String): Resource<DeleteScoreResponse> {
        val response = ApiClient.getmRetrofitInstance().deleteTestScoreById(testId)
        return if (response.isSuccessful && response.body() != null && !response.body()!!.error) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(null, ApiError(response.message(), response.code()))
        }
    }
}