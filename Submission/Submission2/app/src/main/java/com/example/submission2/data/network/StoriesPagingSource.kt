package com.example.submission2.data.network

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submission2.data.network.response.Story
import com.example.submission2.ui.view.main.Location

class StoriesPagingSource(private val apiService: APIService, private val bearerToken: String) :
    PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(
                APIUtils.formatBearerToken(bearerToken),
                position,
                params.loadSize,
                Location.LOCATION_OFF.isOn
            ).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_PAGE_INDEX = 1

        fun snapshot(stories: List<Story>): PagingData<Story> {
            return PagingData.from(stories)
        }
    }
}