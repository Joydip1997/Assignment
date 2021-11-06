package com.example.assignment.UI.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.R
import com.example.assignment.UI.Adapters.TestSeriesAdapter.TestSeriesAdapterViewHolder
import com.example.assignment.UI.OnMenuItemClickListener
import com.example.assignment.Utils.CommonUtils
import com.example.assignment.databinding.TestItemLayoutBinding

class TestSeriesAdapter(private val context: Context) :
    PagingDataAdapter<TestScore, TestSeriesAdapterViewHolder>(ORDER_COMPARATOR) {

    companion object {
        private val ORDER_COMPARATOR = object : DiffUtil.ItemCallback<TestScore>() {
            override fun areItemsTheSame(
                oldItem: TestScore,
                newItem: TestScore
            ): Boolean {
                return true
            }


            override fun areContentsTheSame(
                oldItem: TestScore,
                newItem: TestScore
            ): Boolean =
                oldItem == newItem
        }
    }

    private lateinit var mListener: OnMenuItemClickListener

    fun setMenuItemClickListener(mListener: OnMenuItemClickListener) {
        this.mListener = mListener
    }


    inner class TestSeriesAdapterViewHolder(val binding: TestItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestSeriesAdapterViewHolder {
        val binding =
            TestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TestSeriesAdapterViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TestSeriesAdapterViewHolder, position: Int) {
        val testItem = getItem(position)!!
        val dateObject = CommonUtils.convertTimeFromString(testItem.testDate)
        holder.binding.apply {
            testItemTitle.text = testItem.testName
            dateTv.text = "${dateObject.year} " +
                    "${CommonUtils.getFormattedMonth(dateObject.month.toString())} " +
                    CommonUtils.getFormattedDay(dateObject.dayOfMonth)
            testSeriesNameTv.text = testItem.testSeries
            subPhysics.apply {
                subjectNameTv.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_physics),
                        null,
                        null,
                        null
                    )
                    text = "Physics"
                }
                subjectScoreTv.text = "${testItem.scores.Physics}/100"
            }
            subChem.apply {
                subjectNameTv.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_chemistry),
                        null,
                        null,
                        null
                    )
                    text = "Chemistry"
                }
                subjectScoreTv.text = "${testItem.scores.Chemistry}/100"
            }
            subMath.apply {
                subjectNameTv.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_maths),
                        null,
                        null,
                        null
                    )
                    text = "Mathematics"
                }
                subjectScoreTv.text = "${testItem.scores.Mathematics}/100"
            }
            total.apply {
                subjectNameTv.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                    text = "Total"
                }
                val total =
                    testItem.scores.Physics + testItem.scores.Chemistry + testItem.scores.Mathematics
                subjectScoreTv.text = "${total}/300"
            }
            menuOptions.setOnClickListener {
                mListener.onMenuOptionClick(position,testItem)
            }
        }
    }


}