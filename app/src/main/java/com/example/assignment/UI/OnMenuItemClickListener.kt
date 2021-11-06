package com.example.assignment.UI

import com.example.assignment.Data.Model.TestScore

interface OnMenuItemClickListener {

    fun onMenuOptionClick(postion : Int,testScore: TestScore)
}