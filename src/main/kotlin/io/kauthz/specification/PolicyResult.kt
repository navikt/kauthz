package io.kauthz.specification

// TODO check results from operators
enum class PolicyResult {
    PERMIT {
        override infix fun and(other: PolicyResult): PolicyResult = other
        override infix fun or(other: PolicyResult): PolicyResult = PERMIT
        override fun not(): PolicyResult = DENY
    },

    DENY {
        override infix fun and(other: PolicyResult): PolicyResult = DENY
        override infix fun or(other: PolicyResult): PolicyResult = other
        override fun not(): PolicyResult = PERMIT
    },

    NOT_APPLICABLE {
        override infix fun and(other: PolicyResult): PolicyResult = if (other == PERMIT) NOT_APPLICABLE else other
        override infix fun or(other: PolicyResult): PolicyResult = if (other == DENY) NOT_APPLICABLE else other
        override fun not(): PolicyResult = NOT_APPLICABLE
    };

    abstract infix fun and(other: PolicyResult): PolicyResult
    abstract infix fun or(other: PolicyResult): PolicyResult
    abstract fun not(): PolicyResult
}
