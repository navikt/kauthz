package io.kauthz.specification

// TODO check results from operators
enum class Result {
    YES {
        override infix fun and(other: Result): Result = other
        override infix fun or(other: Result): Result = YES
        override fun not(): Result = NO
    },

    NO {
        override infix fun and(other: Result): Result = NO
        override infix fun or(other: Result): Result = other
        override fun not(): Result = YES
    },

    UNKNOWN {
        override infix fun and(other: Result): Result = if (other == YES) UNKNOWN else other
        override infix fun or(other: Result): Result = if (other == NO) UNKNOWN else other
        override fun not(): Result = UNKNOWN
    };

    abstract infix fun and(other: Result): Result
    abstract infix fun or(other: Result): Result
    abstract fun not(): Result
}
