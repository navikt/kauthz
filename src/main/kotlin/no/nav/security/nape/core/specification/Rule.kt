package no.nav.security.nape.core.specification

import no.nav.security.nape.core.specification.RuleEvaluation.Companion.no

data class Rule<T>(
    val id: String = "",
    val description: String,
    val implementation: T.() -> RuleEvaluation
) {
    fun evaluate(t: T): RuleEvaluation {
        return RuleEvaluation.evaluate(
            id = id,
            description = description,
            eval = t.implementation()
        )
    }

    infix fun and(other: Rule<T>): Rule<T> {
        return Rule(
            description = "$description AND ${other.description}",
            implementation = { evaluate(this) and other.evaluate(this) }
        )
    }

    infix fun or(other: Rule<T>): Rule<T> {
        return Rule(
            description = "$description OR ${other.description}",
            implementation = { evaluate(this) or other.evaluate(this) }
        )
    }

    operator fun not(): Rule<T> {
        return Rule(
            description = "!$description",
            id = "!$id",
            implementation = { evaluate(this).not() }
        )
    }

    fun with(identifikator: String, beskrivelse: String): Rule<T> {
        return this.copy(id = identifikator, description = beskrivelse)
    }

    private constructor(builder: Builder<T>) : this(builder.id, builder.description, builder.implementation)

    companion object {
        inline fun <T> rule(block: Builder<T>.() -> Unit) = Builder<T>().apply(block).build()
    }

    class Builder<T> {
        var id: String = ""
        var description: String = ""
        var implementation: T.() -> RuleEvaluation = { no("not implemented") }
        fun build() = Rule(this)
    }
}

fun <T> not(spec: Rule<T>) = spec.not()
