package io.kauthz.specification

data class PolicyEvaluation(
    val result: PolicyResult,
    val reason: String,
    val description: String = "",
    val id: String = "",
    val operator: Operator = Operator.NONE,
    var children: List<PolicyEvaluation> = emptyList()
) {

    infix fun and(other: PolicyEvaluation) = PolicyEvaluation(
        result = result and other.result,
        reason = "($reason AND ${other.reason})",
        operator = Operator.AND,
        children = this.specOrChildren() + other.specOrChildren()
    )

    infix fun or(other: PolicyEvaluation) = PolicyEvaluation(
        result = result or other.result,
        reason = "($reason OR ${other.reason})",
        operator = Operator.OR,
        children = this.specOrChildren() + other.specOrChildren()
    )

    operator fun not() = PolicyEvaluation(
        result = result.not(),
        reason = "(NOT $reason)",
        operator = Operator.NOT,
        children = listOf(this)
    )

    private fun specOrChildren(): List<PolicyEvaluation> =
        if (id.isBlank() && children.isNotEmpty()) children else listOf(this)

    companion object {
        fun permit(reason: String = "") = PolicyEvaluation(PolicyResult.PERMIT, reason)
        fun deny(reason: String) = PolicyEvaluation(PolicyResult.DENY, reason)

        fun notapplicable(reason: String) = PolicyEvaluation(PolicyResult.NOT_APPLICABLE, reason)

        fun evaluate(id: String, description: String, eval: PolicyEvaluation) = eval.copy(id = id, description = description)
    }
}

infix fun PolicyEvaluation.`is`(result: PolicyResult): Boolean = this.result == result
