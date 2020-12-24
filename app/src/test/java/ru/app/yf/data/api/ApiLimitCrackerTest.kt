package ru.app.yf.data.api

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ApiLimitCrackerTest {

    val apiLimitCracker:ApiLimitCracker =  ApiLimitCracker

    @Test
    fun `Api limit cracker should count quantity of available api keys`(){
        assertEquals(apiLimitCracker.getSizeMapOfApiKeys(), apiLimitCracker.getCountOfAvaliableApiKeys())
        apiLimitCracker.getNextApiKey()
        assertEquals(apiLimitCracker.getSizeMapOfApiKeys() - 1, apiLimitCracker.getCountOfAvaliableApiKeys())
    }

    @Test
    fun `Api limit cracker should return available api keys`(){
        assertNotNull(apiLimitCracker.getNextApiKey())
        assertNotNull(apiLimitCracker.getNextApiKey())
        assertNull(apiLimitCracker.getNextApiKey())
    }
}