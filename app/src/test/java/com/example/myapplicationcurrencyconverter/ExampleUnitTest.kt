package com.example.myapplicationcurrencyconverter

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testConversion1_USD_to_EUR() {
        val resultFormatted = CurrencyConverter.convertirEtFormater(120.0, "USD", "EUR")
        assertEquals("102.56", resultFormatted)
    }

    @Test
    fun testConversion2_EUR_to_TND() {
        val resultFormatted = CurrencyConverter.convertirEtFormater(100.0, "EUR", "TND")
        assertEquals("340.00", resultFormatted)
    }

    @Test
    fun testConversion3_GBP_to_USD() {
        val resultFormatted = CurrencyConverter.convertirEtFormater(50.0, "GBP", "USD")
        assertEquals("67.24", resultFormatted)
    }

    @Test
    fun testConversion4_EUR_to_EUR() {
        val resultFormatted = CurrencyConverter.convertirEtFormater(10.0, "EUR", "EUR")
        assertEquals("10.00", resultFormatted)
    }
}