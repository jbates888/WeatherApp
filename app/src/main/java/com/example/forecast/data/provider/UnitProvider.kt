package com.example.forecast.data.provider

import com.example.forecast.internal.UnitSystem

/*
 * interface for unit system (metric or imperial)
 */
interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}