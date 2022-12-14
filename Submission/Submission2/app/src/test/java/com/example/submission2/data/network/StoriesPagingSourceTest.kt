package com.example.submission2.data.network

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submission2.data.network.response.Story

class StoriesPagingSourceTest :
    PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = 0,
            nextKey = 1
        )
    }

    companion object {
        fun snapshot(stories: List<Story>): PagingData<Story> {
            return PagingData.from(stories)
        }
    }
}