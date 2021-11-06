package com.example.assignment.Data.Model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.assignment.Utils.CommonUtils

data class TestScore(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val exam: String,
    val scores: Scores,
    var testDate: String,
    val testName: String,
    val testSeries: String,
    val updatedAt: String
){

    fun formatDate(formattedDate : String){
        testDate = formattedDate
    }
}