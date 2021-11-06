package com.example.assignment.Data.Model

data class TestListResponse(
    val count: Int,
    val error: Boolean,
    val limit: Int,
    val page: Int,
    val testScores: ArrayList<TestScore>,
    val totalCount: Int
)