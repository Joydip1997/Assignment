package com.example.assignment.Retrofit

import com.example.assignment.Data.Model.DeleteScoreResponse
import com.example.assignment.Data.Model.TestScoreModel.TestScoreResponse
import com.example.assignment.Data.Model.TestListResponse
import com.example.assignment.Data.Model.TestScoreModel.AddTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.EditTestScoreDto
import com.example.assignment.Data.Model.TestSeriesModel.TestSeriesResponse
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/test-scores")
    suspend fun getAllTestScoresPage(
        @Query("email") email: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): TestListResponse

    @GET("/test-scores")
    suspend fun getAllTestScores(
        @Query("email") email: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<TestListResponse>

    @GET("/test-series")
    suspend fun getTestSeries() : Response<TestSeriesResponse>

    @GET("/test-scores/{id}")
    suspend fun getTestScores(
        @Path("id") testId : String
    ) : Response<TestScoreResponse>

    @PATCH("/test-scores/{id}")
    suspend fun editTestScoreById(
        @Path("id") testId : String,
        @Body editTestScoreDto: EditTestScoreDto
    ) : Response<TestScoreResponse>

    @POST("/test-scores")
    suspend fun addTestScore(
        @Body addTestScoreDto: AddTestScoreDto
    ) : Response<TestScoreResponse>

    @DELETE("/test-scores/{id}")
    suspend fun deleteTestScoreById(
        @Path("id") testId : String
    ) : Response<DeleteScoreResponse>


}