package com.example.assignment.Data.Model.TestScoreModel

data class AddTestScoreDto(
    val email: String,
    val exam: String,
    val scores: Scores,
    val testDate: String,
    val testName: String,
    val testSeries: String
)