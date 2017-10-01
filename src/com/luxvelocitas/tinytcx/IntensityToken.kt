package com.luxvelocitas.tinytcx

enum class IntensityToken {
    Active, Resting;

    companion object {
        fun fromString(s: String?): IntensityToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}