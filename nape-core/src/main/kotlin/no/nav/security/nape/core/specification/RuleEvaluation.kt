package no.nav.security.nape.core.specification

data class RuleEvaluation(
    val result: Result,
    val reason: String,
    val description: String = "",
    val id: String = "",
    val operator: Operator = Operator.NONE
) {

    fun isYes(): Boolean = this.result == Result.YES
    fun isNo(): Boolean = this.result == Result.NO
    fun isUnknown(): Boolean = this.result == Result.UNKNOWN

    infix fun and(other: RuleEvaluation) = RuleEvaluation(
        result = result and other.result,
        reason = "($reason AND ${other.reason})",
        operator = Operator.AND
    )

    infix fun or(other: RuleEvaluation) = RuleEvaluation(
        result = result or other.result,
        reason = "($reason OR ${other.reason})",
        operator = Operator.OR
    )

    fun not() = RuleEvaluation(
        result = result.not(),
        reason = "(NOT $reason)",
        operator = Operator.NOT
    )

    companion object {
        fun yes(reason: String = "") = RuleEvaluation(Result.YES, reason)

        fun no(reason: String) = RuleEvaluation(Result.NO, reason)

        fun unknown(reason: String) = RuleEvaluation(Result.UNKNOWN, reason)

        fun evaluate(id: String, description: String, eval: RuleEvaluation) = eval.copy(id = id, description = description)
    }
}
