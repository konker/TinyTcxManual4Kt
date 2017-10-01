package com.luxvelocitas.tinytcx

enum class TrainingTypeToken {
    Workout, Course;

    companion object {
        fun fromString(s: String?): TrainingTypeToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}