package com.luxvelocitas.tinytcx

enum class SportToken {
    Running, Biking, Other;

    companion object {
        fun fromString(s: String?): SportToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}