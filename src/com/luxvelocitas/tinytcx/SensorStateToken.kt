package com.luxvelocitas.tinytcx

enum class SensorStateToken {
    Present, Absent;

    companion object {
        fun fromString(s: String?): SensorStateToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}