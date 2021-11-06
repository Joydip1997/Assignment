package com.example.assignment.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.R
import com.example.assignment.UI.Adapters.TestSeriesAdapter
import com.example.assignment.UI.ViewModels.TestSeriesFragmentViewModel
import com.example.assignment.Utils.collectWhileStarted
import com.example.assignment.Utils.snackBar
import com.example.assignment.databinding.FragmentTestListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException


@AndroidEntryPoint
class FragmentTestList : Fragment(R.layout.fragment_test_list) {
    private lateinit var testSeriesAdapter: TestSeriesAdapter
    private var _binding: FragmentTestListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TestSeriesFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTestListBinding.bind(view)
        testSeriesAdapter =
            TestSeriesAdapter(requireContext())
        testSeriesAdapter.setMenuItemClickListener(object :
            OnMenuItemClickListener {
            override fun onMenuOptionClick(postion: Int, testScore: TestScore) {
                performOptionsMenuClick(postion, testScore)
            }
        })
        initAdapter()
        subscribeToObservers()
        binding.addNewTestScoreTv.setOnClickListener {
            findNavController().navigate(
                FragmentTestListDirections.actionFragmentTestListToFragmentTestAddEdit(
                    "", true, ""
                )
            )
        }
        fetchTestScoreList()
    }

    private fun fetchTestScoreList() {
        lifecycleScope.launch {
            viewModel.fetchTestSeries(
                "noneuser2183@gmail.com",
            ).collectLatest {
                binding.apply {
                    testSeriesAdapter.submitData(it)
                }
            }
        }
    }

    private fun initAdapter() {
        binding.testSeriesRView.apply {
            adapter = testSeriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        testSeriesAdapter.addLoadStateListener { loadState ->
            toggleLayout(loadState.source.refresh is LoadState.Loading)
            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                when (it.error) {

                    is HttpException -> {
                        snackBar(
                            "Something went wrong",
                            Snackbar.LENGTH_SHORT
                        )
                    }

                }
            }
        }

        // Scroll to top when the list is refreshed from network.
        //We will use it later with ROOM database
//        lifecycleScope.launch {
//            adapter.loadStateFlow
//                .distinctUntilChangedBy { it.refresh }
//                .filter { it.refresh is LoadState.NotLoading }
//                .collect { binding.myOrdersList.scrollToPosition(0) }
//        }
    }

    @SuppressLint("RestrictedApi")
    private fun performOptionsMenuClick(position: Int, testScore: TestScore) {
        val popupMenu = PopupMenu(
            requireContext(),
            binding.testSeriesRView[position].findViewById(R.id.menu_options)
        )
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    findNavController().navigate(
                        FragmentTestListDirections.actionFragmentTestListToFragmentTestAddEdit(
                            testScore._id, false, testScore.testSeries
                        )
                    )
                }
                R.id.menu_delete -> {
                    viewModel.deleteTestScoreById(testScore._id)
                }
            }
            false
        }

//        val menuHelper = MenuPopupHelper(requireContext(),MenuBuilder(requireContext()))
//        menuHelper.setAnchorView(requireView().)
//        menuHelper.setForceShowIcon(true)
//        menuHelper.show()
        popupMenu.show()
    }

    private fun subscribeToObservers() {
        viewModel.apply {
            loadingEvent.collectWhileStarted(viewLifecycleOwner) {
                if (it) {
                    toggleLayout(true)
                } else {
                    toggleLayout(false)
                }
            }
            deleteScoreEvent.collectWhileStarted(viewLifecycleOwner) {
                if (it) {
                    fetchTestScoreList()
                    snackBar("Test score deleted", Snackbar.LENGTH_SHORT)
                } else {
                    snackBar("Something went wrong", Snackbar.LENGTH_SHORT)
                }
            }
        }
    }

    private fun toggleLayout(toggle: Boolean) {
        binding.apply {
            mainScreenShimmerLayout.isVisible = toggle
            testSeriesRView.isVisible = !toggle
        }
    }

}