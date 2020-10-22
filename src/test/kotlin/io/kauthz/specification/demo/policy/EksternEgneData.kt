package io.kauthz.specification.demo.policy

import io.kauthz.specification.Policy
import io.kauthz.specification.Policy.Companion.policy
import io.kauthz.specification.PolicyEvaluation
import io.kauthz.specification.Rule
import io.kauthz.specification.Rule.Companion.rule
import io.kauthz.specification.RuleEvaluation.Companion.no
import io.kauthz.specification.RuleEvaluation.Companion.yes
import io.kauthz.specification.demo.AccessContext

val `tilgang til opplysninger om seg selv`: Policy<AccessContext> =
    policy {
        id = "ekstern_tilgang_opplysninger_om_seg_selv"
        description = "ekstern_tilgang_opplysninger_om_seg_selv"
        rules = {
            val ruleEvaluation = `subject er lik person`.evaluate(this)
            if (ruleEvaluation.isYes()) {
                PolicyEvaluation.permit(ruleEvaluation.reason)
            } else {
                PolicyEvaluation.deny(ruleEvaluation.reason)
            }
        }
    }

val `subject er lik person`: Rule<AccessContext> =
    rule {
        id = "seg_selv"
        description = "opplysninger om seg selv"
        implementation = {
            if (subject.id == person.fnr) {
                yes()
            } else {
                no("subject id er ikke lik person fnr, person fnr=${person.fnr}")
            }
        }
    }
