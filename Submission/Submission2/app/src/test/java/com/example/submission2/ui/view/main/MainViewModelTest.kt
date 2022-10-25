package com.example.submission2.ui.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.data.network.Result
import com.example.submission2.adapter.StoryListAdapter
import com.example.submission2.data.network.StoriesPagingSource
import com.example.submission2.data.network.response.Story
import com.example.submission2.data.network.response.StoryResponse
import com.example.submission2.data.repository.AuthRepository
import com.example.submission2.data.repository.StoryRepository
import com.example.submission2.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    private val dummyBearerToken: String = DummyDataGenerator.generateDummyBearerToken()
    private val dummyPage: Int? = null
    private val dummySize: Int = 50

    @Before
    fun setup() {
        storyRepository = Mockito.mock(StoryRepository::class.java)
        mainViewModel = MainViewModel(authRepository, storyRepository)
    }

    @Test
    fun `When Getting Bearer Token Should Not Null And Equal Dummy`() = runTest {
        val expectedData = MutableLiveData<String?>()
        expectedData.value = dummyBearerToken

        Mockito.`when`(
            authRepository.getBearerToken()
        ).thenReturn(expectedData)

        val actualData = mainViewModel.getBearerToken().getOrAwaitValue()

        Mockito.verify(authRepository).getBearerToken()

        assertNotNull(actualData)
        assertEquals(dummyBearerToken, actualData)
    }

    @Test
    fun `When Successfully Getting Data For Main Menu Should Not Null And Equal Dummy`() =
        runTest {
            val expectedData = MutableLiveData<PagingData<Story>>()
            expectedData.value =
                StoriesPagingSource.snapshot(DummyDataGenerator.generateDummyStories())

            Mockito.`when`(storyRepository.getPagedStories(DummyDataGenerator.generateDummyBearerToken()))
                .thenReturn(
                expectedData
            )

            val actualData =
                mainViewModel.getMainStories(DummyDataGenerator.generateDummyBearerToken())
                    .getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = object : ListUpdateCallback {
                    override fun onInserted(position: Int, count: Int) {}
                    override fun onRemoved(position: Int, count: Int) {}
                    override fun onMoved(fromPosition: Int, toPosition: Int) {}
                    override fun onChanged(position: Int, count: Int, payload: Any?) {}
                },
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(actualData)

            Mockito.verify(storyRepository).getPagedStories(DummyDataGenerator.generateDummyBearerToken())

            assertNotNull(actualData)
            assertEquals(DummyDataGenerator.generateDummyStories(), differ.snapshot())
            assertEquals(DummyDataGenerator.generateDummyStories().size, differ.snapshot().size)
        }

    @Test
    fun `When Successfully Getting Data For Maps Should Not Null And Equal Dummy`() = runTest {
        val expectedData = MutableLiveData<Result<StoryResponse>>()
        expectedData.value = Result.Success(DummyDataGenerator.generateDummySuccessStoryResponse())

        Mockito.`when`(
            storyRepository.getStories(
                dummyBearerToken,
                dummyPage,
                dummySize,
                Location.LOCATION_ON
            )
        ).thenReturn(expectedData)

        val actualData = mainViewModel.getMapsStories(
            dummyBearerToken,
            dummySize
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).getStories(
            dummyBearerToken,
            dummyPage,
            dummySize,
            Location.LOCATION_ON
        )

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            Result.Success(DummyDataGenerator.generateDummySuccessStoryResponse()),
            actualData
        )
    }

    @Test
    fun `When Logout Should Call Logout Method On authRepository`() = runTest {
        mainViewModel.logout()
        Mockito.verify(authRepository).logout()
    }
}
