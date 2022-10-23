package com.example.submission2.ui.view.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.response.CreateStoryResponse
import com.example.submission2.data.repository.AuthRepository
import com.example.submission2.data.repository.StoryRepository
import com.example.submission2.getOrAwaitValue
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
class CreateStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var createStoryViewModel: CreateStoryViewModel

    private val dummyBearerToken: String = DummyDataGenerator.generateDummyBearerToken()
    private val dummyDescription: RequestBody = DummyDataGenerator.generateDummyDescription()
    private val dummyFile: MultipartBody.Part = DummyDataGenerator.generateDummyFileMultipart()

    @Before
    fun setup() {
        createStoryViewModel = CreateStoryViewModel(authRepository, storyRepository)
    }

    @Test
    fun `When Getting Bearer Token Should Not Null and equal dummy`() = runTest {
        val expectedData = MutableLiveData<String?>()
        expectedData.value = dummyBearerToken

        Mockito.`when`(
            authRepository.getBearerToken()
        ).thenReturn(expectedData)

        val actualData = createStoryViewModel.getBearerToken().getOrAwaitValue()

        Mockito.verify(authRepository).getBearerToken()

        assertNotNull(actualData)
        assertEquals(dummyBearerToken, actualData)
    }

    @Test
    fun `When Submit Is Success Data Should Not Null and is ResultSuccess`() = runTest {
        val expectedData = MutableLiveData<Result<CreateStoryResponse>>()
        expectedData.value =
            Result.Success(DummyDataGenerator.generateDummySuccessCreateStoryResponse())

        Mockito.`when`(
            storyRepository.submit(
                dummyBearerToken,
                dummyDescription,
                dummyFile,
                null,
                null
            )
        ).thenReturn(expectedData)

        val actualData = createStoryViewModel.submit(
            dummyBearerToken,
            dummyDescription,
            dummyFile,
            null,
            null
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).submit(
            dummyBearerToken,
            dummyDescription,
            dummyFile,
            null,
            null
        )

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            Result.Success(DummyDataGenerator.generateDummySuccessCreateStoryResponse()),
            actualData
        )
    }

    @Test
    fun `When Submit Is Failed Data Should Not Null and is ResultError`() = runTest {
        val expectedData = MutableLiveData<Result<CreateStoryResponse>>()
        expectedData.value = Result.Error(DummyDataGenerator.generateDummyException())

        Mockito.`when`(
            storyRepository.submit(
                dummyBearerToken,
                dummyDescription,
                dummyFile,
                null,
                null
            )
        ).thenReturn(expectedData)

        val actualData = createStoryViewModel.submit(
            dummyBearerToken,
            dummyDescription,
            dummyFile,
            null,
            null
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).submit(
            dummyBearerToken,
            dummyDescription,
            dummyFile,
            null,
            null
        )

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }
}