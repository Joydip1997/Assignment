package com.example.assignment.Data.Model.TestScoreModel

import com.example.assignment.Data.Model.TestScore

data class TestScoreResponse(
    val error: Boolean,
    val testScore: TestScore
)