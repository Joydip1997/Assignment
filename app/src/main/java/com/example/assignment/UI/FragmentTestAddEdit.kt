package com.example.assignment.UI

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignment.Data.Model.TestScoreModel.EditTestScoreDto
import com.example.assignment.Data.Model.TestScoreModel.Scores
import com.example.assignment.R
import com.example.assignment.UI.ViewModels.AddEditTestScoreFragmentViewModel
import com.example.assignment.Utils.collectWhileStarted
import com.example.assignment.Utils.snackBar
import com.example.assignment.databinding.FragmentAddEditTestBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

@AndroidEntryPoint
class FragmentTestAddEdit : Fragment(R.layout.fragment_add_edit_test) {
    private var _binding: FragmentAddEditTestBinding? = null
    private val binding get() = _binding!!
    private val viewModelAddEdit: AddEditTestScoreFragmentViewModel by viewModels()
    private val args: FragmentTestAddEditArgs by navArgs()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditTestBinding.bind(view)
        lifecycle.addObserver(viewModelAddEdit)
        if (!args.addNewTestScore) {
            binding.apply {
                title.text = "Edit Test Score"
                title2.text = "Edit ${args.testName} test scores "
            }
            viewModelAddEdit.getTestScoresById(args.testId)
        }
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addTakenDateEt.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select date")
                    .build()
            datePicker.show(fragmentManager!!,"TAG")
            datePicker.addOnPositiveButtonClickListener {
                val date = getDate(datePicker.selection!!, "MM/dd/yyyy")
                binding.addTakenDateEt.text = date
            }
        }

        subscribeToObservers()
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


    private fun subscribeToObservers() {
        viewModelAddEdit.apply {
            testScoreState.collectWhileStarted(viewLifecycleOwner) {
                when (it) {
                    is AddEditTestScoreFragmentViewModel.TestScoreState.TestScoresAvailable -> {
                        binding.apply {

                            this.testSeriesLayout.isEnabled = false
                            this.testSeriesLayout.setText(it.testScore.testSeries)
                            this.addEditTestName.isEnabled = false
                            this.addEditTestName.setText(it.testScore.testName)
                            this.addTakenDateEt.isEnabled = false
                            this.addTakenDateEt.text = it.testScore.testDate


                            this.physicsCb.isEnabled = false
                            this.chemCb.isEnabled = false
                            this.mathsCb.isEnabled = false

                            this.physicsCb.isChecked = it.testScore.scores.Physics > 0
                            this.chemCb.isChecked = it.testScore.scores.Chemistry > 0
                            this.mathsCb.isChecked = it.testScore.scores.Mathematics > 0

                            this.physicsYourScoreEt.setText(it.testScore.scores.Physics.toString())
                            this.chemYourScoreEt.setText(it.testScore.scores.Chemistry.toString())
                            this.mathsYourScoreEt.setText(it.testScore.scores.Mathematics.toString())



                        }
                    }
                    is AddEditTestScoreFragmentViewModel.TestScoreState.Error -> {

                    }

                    else -> Unit
                }
            }
            testSeriesState.collectWhileStarted(viewLifecycleOwner) {
                when (it) {
                    is AddEditTestScoreFragmentViewModel.TestSeriesState.TestSeriesStateAvailable -> {
                       if(args.addNewTestScore){
                           val newList = it.testSeries.toMutableList()
                           newList.add(0,"Select Test Series")
                           val arrayAdapter = ArrayAdapter(
                               requireContext(),
                               R.layout.test_series_item_layout,
                               newList as List<String>
                           )
                           binding.testSpinner.adapter = arrayAdapter
                           binding.testSpinner.setSelection(0)
                           binding.testSpinner.onItemSelectedListener = object :
                               AdapterView.OnItemSelectedListener {
                               override fun onItemSelected(
                                   parent: AdapterView<*>,
                                   view: View?,
                                   position: Int,
                                   id: Long
                               ) {
                                   val selectedItem = parent.getItemAtPosition(position) as String
                                   if(position == 0){
                                       binding.saveTestScore.isEnabled = false
                                   }else{
                                       binding.saveTestScore.isEnabled = true
                                       selectTestSeries(selectedItem)
                                   }

                               }
                               override fun onNothingSelected(parent: AdapterView<*>?) {}
                           }
                       }


                        binding.saveTestScore.setOnClickListener {
                           if(args.addNewTestScore){
                               addEditTestName(binding.addEditTestName.text.toString())
                               addEditTestDate(binding.addTakenDateEt.text.toString())
                               addEditTestScores(
                                   binding.physicsYourScoreEt.text.toString(),
                                   binding.chemYourScoreEt.text.toString(),
                                   binding.mathsYourScoreEt.text.toString()
                               )
                               addNewTestScore()
                           }else{
                               val editTestScoreDto = EditTestScoreDto(
                                   Scores(
                                       binding.chemYourScoreEt.text.toString().toInt(),
                                       binding.mathsYourScoreEt.text.toString().toInt(),
                                       binding.physicsYourScoreEt.text.toString().toInt(),
                                   )
                               )
                               viewModelAddEdit.editTestScore(args.testId, editTestScoreDto)
                           }
                        }
                    }
                    is AddEditTestScoreFragmentViewModel.TestSeriesState.Error -> {

                    }
                    else -> Unit
                }
            }
            addEditTestEvent.collectWhileStarted(viewLifecycleOwner) {
                if(it){
                    snackBar("Test Score Saved", Snackbar.LENGTH_SHORT)
                    findNavController().popBackStack()
                }else{
                    snackBar("Please enter all the details", Snackbar.LENGTH_SHORT)
                }
            }
            loadingEvent.collectWhileStarted(viewLifecycleOwner) {
                if (it) {
                    toggleLayout(true)
                } else {
                    toggleLayout(false)
                }
            }
        }
    }

    private fun toggleLayout(toggle: Boolean) {
        binding.apply {
            progressCircular.isVisible = toggle
            mainContent.isVisible = !toggle
            saveTestScore.isVisible = !toggle
        }
    }


}