package com.example.assignment.Data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignment.Data.Model.TestListResponse
import com.example.assignment.Data.Model.TestScore
import com.example.assignment.Retrofit.ApiClient
import retrofit2.HttpException
import java.io.IOException

private const val ORDER_LIST_STARTING_PAGE_INDEX = 0

class TestScoreListPagingSource(
    private val email: String
) : PagingSource<Int, TestScore>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TestScore> {
        val position = params.key ?: ORDER_LIST_STARTING_PAGE_INDEX
        return try {
            val response =
                ApiClient.getmRetrofitInstance()
                    .getAllTestScoresPage(email, position, params.loadSize)


           val repos = response.testScores

            LoadResult.Page(
                data = repos,
                prevKey = if (position == ORDER_LIST_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, TestScore>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}