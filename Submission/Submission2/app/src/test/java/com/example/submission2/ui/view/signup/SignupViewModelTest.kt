package com.example.submission2.ui.view.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.response.RegisterResponse
import com.example.submission2.data.repository.AuthRepository
import com.example.submission2.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
class SignupViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: SignupViewModel

    private val dummyName = "john doe"
    private val dummyEmail = "john@doe.com"
    private val dummyPassword = "pa$\$word"

    @Before
    fun setup() {
        registerViewModel = SignupViewModel(authRepository)
    }

    @Test
    fun `When Register Success Data Should Not Null and is ResultSuccess`() = runTest {
        val expectedData = MutableLiveData<Result<RegisterResponse>>()
        expectedData.value =
            Result.Success(DummyDataGenerator.generateDummySuccessRegisterResponse())

        Mockito.`when`(authRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedData
        )

        val actualData =
            registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(authRepository).register(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            Result.Success(DummyDataGenerator.generateDummySuccessRegisterResponse()),
            actualData
        )
    }

    @Test
    fun `When Register Failed Data Should Not Null and is ResultError`() = runTest {
        val expectedData = MutableLiveData<Result<RegisterResponse>>()
        expectedData.value = Result.Error(DummyDataGenerator.generateDummyException())

        Mockito.`when`(authRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedData
        )

        val actualData =
            registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(authRepository).register(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }
}