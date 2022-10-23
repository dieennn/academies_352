package com.example.submission2.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.adapter.StoryListAdapter
import com.example.submission2.data.network.APIService
import com.example.submission2.data.network.APIUtils
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.StoriesPagingSourceTest
import com.example.submission2.data.network.response.Story
import com.example.submission2.getOrAwaitValue
import com.example.submission2.ui.view.main.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var apiService: APIService
    private lateinit var storyRepository: StoryRepository

    private val dummyAuthorization =
        APIUtils.formatBearerToken(DummyDataGenerator.generateDummyBearerToken())
    private val dummyBearerToken = DummyDataGenerator.generateDummyBearerToken()
    private val dummyPage: Int? = null
    private val dummySize: Int = 50

    private val dummyDescription: RequestBody = DummyDataGenerator.generateDummyDescription()
    private val dummyFile: MultipartBody.Part = DummyDataGenerator.generateDummyFileMultipart()

    @Before
    fun setup() {
        storyRepository = StoryRepository(apiService)
    }

    @Test
    fun `When Successfully Getting Data For Main Menu Should Not Null and Equal Dummy`() = runTest {
        val mockedClass = Mockito.mock(StoryRepository::class.java)

        val expectedData = MutableLiveData<PagingData<Story>>()
        expectedData.value =
            StoriesPagingSourceTest.snapshot(DummyDataGenerator.generateDummyStories())

        Mockito.`when`(mockedClass.getPagedStories(DummyDataGenerator.generateDummyBearerToken())).thenReturn(
            expectedData
        )

        val actualData = mockedClass.getPagedStories(DummyDataGenerator.generateDummyBearerToken())
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

        Mockito.verify(mockedClass).getPagedStories(DummyDataGenerator.generateDummyBearerToken())

        assertNotNull(actualData)
        assertEquals(DummyDataGenerator.generateDummyStories(), differ.snapshot())
        assertEquals(DummyDataGenerator.generateDummyStories().size, differ.snapshot().size)
    }

    @Test
    fun `When Successfully Getting Data For Maps Should Not Null and Equal Dummy`() = runTest {
        val expectedData = DummyDataGenerator.generateDummySuccessStoryResponse()

        Mockito.`when`(
            apiService.getStories(
                dummyAuthorization,
                dummyPage,
                dummySize,
                Location.LOCATION_ON.isOn
            )
        ).thenReturn(expectedData)

        val actualData =
            storyRepository.getStories(dummyBearerToken, dummyPage, dummySize, Location.LOCATION_ON)
                .apply { getOrAwaitValue() }
                .getOrAwaitValue()

        Mockito.verify(apiService).getStories(
            dummyAuthorization,
            dummyPage,
            dummySize,
            Location.LOCATION_ON.isOn
        )

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            DummyDataGenerator.generateDummySuccessStoryResponse(),
            (actualData as Result.Success).data
        )
    }

    @Test
    fun `When Not Success Getting Data For Maps Should Not Null and is ResultError`() = runTest {
        Mockito.`when`(
            apiService.getStories(
                dummyAuthorization,
                dummyPage,
                dummySize,
                Location.LOCATION_ON.isOn
            )
        ).then {
            throw DummyDataGenerator.generateDummyException()
        }

        val actualData =
            storyRepository.getStories(dummyBearerToken, dummyPage, dummySize, Location.LOCATION_ON)
                .apply { getOrAwaitValue() }
                .getOrAwaitValue()

        Mockito.verify(apiService).getStories(
            dummyAuthorization,
            dummyPage,
            dummySize,
            Location.LOCATION_ON.isOn
        )

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }

    @Test
    fun `When Submit Is Success Data Should Not Null and is ResultSuccess`() = runTest {
        val expectedData = DummyDataGenerator.generateDummySuccessCreateStoryResponse()

        Mockito.`when`(
            apiService.postStory(
                dummyAuthorization,
                dummyDescription,
                dummyFile,
                null,
                null
            )
        ).thenReturn(expectedData)

        val actualData =
            storyRepository.submit(
                dummyBearerToken,
                dummyDescription,
                dummyFile,
                null,
                null
            )
                .apply { getOrAwaitValue() }
                .getOrAwaitValue()

        Mockito.verify(apiService).postStory(
            dummyAuthorization,
            dummyDescription,
            dummyFile,
            null,
            null
        )

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            DummyDataGenerator.generateDummySuccessCreateStoryResponse(),
            (actualData as Result.Success).data
        )
    }

    @Test
    fun `When Submit Is Failed Data Should Not Null and is ResultError`() = runTest {
        Mockito.`when`(
            apiService.postStory(
                dummyAuthorization,
                dummyDescription,
                dummyFile,
                null,
                null
            )
        ).then {
            throw DummyDataGenerator.generateDummyException()
        }

        val actualData =
            storyRepository.submit(
                dummyBearerToken,
                dummyDescription,
                dummyFile,
                null,
                null
            )
                .apply { getOrAwaitValue() }
                .getOrAwaitValue()

        Mockito.verify(apiService).postStory(
            dummyAuthorization,
            dummyDescription,
            dummyFile,
            null,
            null
        )

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }
}