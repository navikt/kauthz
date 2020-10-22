package io.kauthz.specification.demo.policy

import io.kauthz.specification.Policy
import io.kauthz.specification.Policy.Companion.policy
import io.kauthz.specification.PolicyEvaluation.Companion.deny
import io.kauthz.specification.PolicyEvaluation.Companion.permit
import io.kauthz.specification.Result.YES
import io.kauthz.specification.Rule
import io.kauthz.specification.Rule.Companion.rule
import io.kauthz.specification.RuleEvaluation.Companion.no
import io.kauthz.specification.RuleEvaluation.Companion.yes
import io.kauthz.specification.demo.AccessContext
import io.kauthz.specification.demo.SubjectType

val `subject kan behandle person med kode6`: Policy<AccessContext> =
    policy {
        id = "FP1"
        description = "felles policy for adgang til å behandle kode 6 merkede personer"
        rules = {
            val rule = (
                `person har ikke kode6`
                    or (
                    `subject er intern bruker`
                        and `person har kode6`
                        and `subject har rolle for kode6`
                    )
                )
            val ruleEvaluation = rule.evaluate(this)
            if (ruleEvaluation.result == YES) {
                permit(ruleEvaluation.reason)
            } else {
                deny(ruleEvaluation.reason)
            }
        }
    }

private val `person har kode6`: Rule<AccessContext> =
    rule {
        id = "har_person_kode6"
        description = "sjekk om person har kode6"
        implementation = {
            if (person.spesReg == 6) {
                yes("person har kode6")
            } else {
                no("person har ikke kode6")
            }
        }
    }

private val `person har ikke kode6`: Rule<AccessContext> = !`person har kode6`

private val `subject har rolle for kode6`: Rule<AccessContext> =
    rule {
        id = "har_rolle_for_kode6"
        description = "sjekk om subject har rolle for kode6"
        implementation = {
            if (subject.roles.contains("kode6-gruppe")) {
                yes("har kode6 rolle")
            } else {
                no("har ikke kode6 rolle")
            }
        }
    }

private val `subject er intern bruker`: Rule<AccessContext> =
    rule {
        id = "er_intern_bruker"
        description = "sjekker om innlogget bruker er intern bruker"
        implementation = {
            if (subject.type == SubjectType.InternBruker) {
                yes("subject er InternBruker")
            } else {
                no("subject er ikke InternBruker, men ${subject.type}")
            }
        }
    }

internal object Roles {
    const val KODE6_GRUPPE = "kode6-gruppe"
}
