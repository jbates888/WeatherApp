package com.example.forecast.data.provider

import android.content.Context
import com.example.forecast.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

/*
 * Class for unit system (metric or imperial)
 */
class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {
    //get the unit system
    override fun getUnitSystem(): UnitSystem {
        //retrieve the unit system the user selected in the preferences
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}