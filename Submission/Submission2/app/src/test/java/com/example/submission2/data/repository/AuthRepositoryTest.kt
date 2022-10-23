package com.example.submission2.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.submission2.DummyDataGenerator
import com.example.submission2.MainDispatcherRule
import com.example.submission2.data.local.AppPreferences
import com.example.submission2.data.network.APIService
import com.example.submission2.data.network.Result
import com.example.submission2.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: APIService

    @Mock
    private lateinit var appPreferences: AppPreferences
    private lateinit var authRepository: AuthRepository

    private val dummyBearerToken = DummyDataGenerator.generateDummyBearerToken()
    private val dummyName = "john doe"
    private val dummyUserId = "userid"
    private val dummyEmail = "john@doe.com"
    private val dummyPassword = "pa$\$word"

    @Before
    fun setup() {
        authRepository = AuthRepository(apiService, appPreferences)
    }

    @Test
    fun `When Get Token Should Not Null And Equal Dummy`() = runTest {
        val expectedData = flowOf(dummyBearerToken)

        `when`(appPreferences.getTokenPrefs()).thenReturn(expectedData)

        val actualData = authRepository.getBearerToken().getOrAwaitValue()

        verify(appPreferences).getTokenPrefs()

        assertNotNull(actualData)
        assertEquals(dummyBearerToken, actualData)
    }


    @Test
    fun `When Login Failed Should Not Null and is ResultError`() = runTest {
        `when`(
            apiService.login(dummyEmail, dummyPassword)
        ).then {
            throw DummyDataGenerator.generateDummyException()
        }

        val actualData = authRepository.login(
            dummyEmail, dummyPassword)
            .apply {
                getOrAwaitValue()
            }
            .getOrAwaitValue()

        verify(apiService).login(dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }

    @Test
    fun `When Login Success Data Should Not Null and Is ResultSuccess`() = runTest {
        val expectedData = DummyDataGenerator.generateDummySuccessLoginResponse()

        `when`(apiService.login(dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = authRepository.login(dummyEmail, dummyPassword)
            .apply {
                getOrAwaitValue()
            }
            .getOrAwaitValue()

        verify(apiService).login(dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            DummyDataGenerator.generateDummySuccessLoginResponse(),
            (actualData as Result.Success).data
        )
    }


    @Test
    fun `When Logout should Call clearPerfs Method on AppPreferences`() = runTest {
        authRepository.logout()
        verify(appPreferences).clearPrefs()
    }

    @Test
    fun `When Register Success Data Should Not Null and is ResultSuccess`() = runTest {
        val expectedData = DummyDataGenerator.generateDummySuccessRegisterResponse()

        `when`(apiService.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = authRepository.register(dummyName, dummyEmail, dummyPassword)
            .apply {
                getOrAwaitValue()
            }
            .getOrAwaitValue()

        verify(apiService).register(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Success)
        assertEquals(
            DummyDataGenerator.generateDummySuccessRegisterResponse(),
            (actualData as Result.Success).data
        )
    }

    @Test
    fun `When Register Failed Data Should Not Null and is ResultError`() = runTest {
        `when`(
            apiService.register(dummyName, dummyEmail, dummyPassword)
        ).then {
            throw DummyDataGenerator.generateDummyException()
        }

        val actualData = authRepository.register(dummyName, dummyEmail, dummyPassword)
            .apply {
                getOrAwaitValue()
            }
            .getOrAwaitValue()

        verify(apiService).register(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualData)
        assert(actualData is Result.Error)
    }

    @Test
    fun `When Saving Saving Login Info Should Call method saveNamePrefs on AppPreferences`() =
        runTest {
            authRepository.saveLoginInfo(dummyName, dummyUserId, dummyBearerToken)
            verify(appPreferences).saveNamePrefs(dummyName)
            verify(appPreferences).saveUserIDPrefs(dummyUserId)
            verify(appPreferences).saveTokenPrefs(dummyBearerToken)
        }
}