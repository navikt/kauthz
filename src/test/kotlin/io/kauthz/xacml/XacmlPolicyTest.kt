package io.kauthz.xacml

import io.kauthz.specification.Policy
import io.kauthz.specification.Policy.Companion.policy
import io.kauthz.specification.PolicyEvaluation.Companion.deny
import io.kauthz.specification.PolicyEvaluation.Companion.permit
import io.kauthz.specification.Rule
import io.kauthz.specification.Rule.Companion.rule
import io.kauthz.specification.RuleEvaluation.Companion.no
import io.kauthz.specification.RuleEvaluation.Companion.yes
import io.kauthz.specification.requirePermit
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class XacmlPolicyTest {

    private val request = RequestWrapper.of(
        accessSubject = Category.of(
            "subject.subjectId" to "yolo",
            "subject.role" to "role1",
            "subject.role" to "role2"
        ),
        resource = Category.of(
            "resource.resourceId" to "foobar",
            "resource.required_access_level" to "1"
        )
    )

    private val policy: Policy<RequestWrapper> =
        policy {
            id = "main policy"
            description = "gathers all central policies"
            rules = {
                val rule = !resourceRequireAccessLevel1 or
                    (resourceRequireAccessLevel1 and subjectHasRole1)
                val ruleEvaluation = rule.evaluate(this)
                if (ruleEvaluation.isYes()) {
                    permit(ruleEvaluation.reason)
                } else {
                    deny(ruleEvaluation.reason)
                }
            }
        }

    private val subjectHasRole1: Rule<RequestWrapper> =
        rule {
            implementation = {
                if (this.request.accessSubject?.get("subject.role")?.contains("role1") == true) {
                    yes()
                } else {
                    no("does not have role1")
                }
            }
        }

    private val resourceRequireAccessLevel1: Rule<RequestWrapper> =
        rule {
            implementation = {
                if (this.request.resource?.get("resource.required_access_level") == listOf("1")) {
                    yes()
                } else {
                    no("not level 1")
                }
            }
        }


    @Test
    fun `doh`() {
        policy.requirePermit(request) {
            "my receiver"
        } shouldBe "my receiver"

    }

}
