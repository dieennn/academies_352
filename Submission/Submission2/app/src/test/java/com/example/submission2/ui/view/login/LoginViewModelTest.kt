package com.example.submission2.ui.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.response.LoginResponse
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
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyBearerToken = DummyDataGenerator.generateDummyBearerToken()
    private val dummyName = "john doe"
    private val dummyUserId = "userid"
    private val dummyEmail = "john@doe.com"
    private val dummyPassword = "pa$\$word"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `When Login Success Data Should Not Null and is ResultSuccess`() = runTest {
        val expectedData = MutableLiveData<Result<LoginResponse>>()
        expectedData.value = Result.Success(DummyDataGenerator.generateDummySuccessLoginResponse())

        Mockito.`when`(authRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            Result.Success(DummyDataGenerator.generateDummySuccessLoginResponse()),
            actualData
        )
    }

    @Test
    fun `When Login Failed Data Should Not Null and is ResultError`() = runTest {
        val expectedData = MutableLiveData<Result<LoginResponse>>()
        expectedData.value = Result.Error(DummyDataGenerator.generateDummyException())

        Mockito.`when`(authRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }

    @Test
    fun `When Saving Login Info Should Call saveLoginInfo Method on AuthRepository`() = runTest {
        loginViewModel.saveLoginInfo(dummyName, dummyUserId, dummyBearerToken)
        Mockito.verify(authRepository).saveLoginInfo(dummyName, dummyUserId, dummyBearerToken)
    }
}