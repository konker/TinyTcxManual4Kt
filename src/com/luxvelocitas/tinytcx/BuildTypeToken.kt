package com.luxvelocitas.tinytcx

enum class BuildTypeToken {
    Internal, Alpha, Beta, Release;

    companion object {
        fun fromString(s: String?): BuildTypeToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}