package com.luxvelocitas.tinytcx

enum class TriggerMethodToken {
    Manual, Distance, Location, Time, HeartRate;

    companion object {
        fun fromString(s: String?): TriggerMethodToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}