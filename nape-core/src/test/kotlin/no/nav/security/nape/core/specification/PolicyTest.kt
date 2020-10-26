package no.nav.security.nape.core.specification

import no.nav.security.nape.core.specification.Policy.Companion.policy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.Test

internal class PolicyTest {

    private val `string should be yolo`: Policy<String> =
        policy {
            rules = {
                if (this == "yolo") PolicyEvaluation.permit() else PolicyEvaluation.deny("not yolo")
            }
        }

    @Test
    fun `permitOrFail function should return receiver or throw exception if not permit`() {

        `string should be yolo`.requirePermitOrFail("yolo") {
            "my receiver"
        } shouldBe "my receiver"

        shouldThrow<PolicyEvaluationException> {
            `string should be yolo`.requirePermitOrFail("not yolo obviously") {
                "whatevs"
            }
        }
    }

    @Test
    fun `permit should execute lambda and return receiver if result is Permit`() {
        `string should be yolo`.requirePermit("yolo") {
            "my receiver"
        } shouldBe "my receiver"
    }

    @Test
    fun `permit should not execute lambda and return PolicyEvaluation if result is NOT Permit`() {
        `string should be yolo`.requirePermit("not yolo") {
            "my receiver"
        } should beInstanceOf<PolicyEvaluation>()

        (`string should be yolo`.requirePermit("not yolo") {
            "my receiver"
        } as PolicyEvaluation).decision shouldBe PolicyDecision.DENY
    }

    @Test
    fun `authorize should execute block and return receiver`() {
        authorize("yolo", `string should be yolo`) {
            if (it.decision == PolicyDecision.PERMIT) {
                "my receiver"
            } else {
                null
            }
        } shouldBe "my receiver"
    }

    @Test
    fun `authorize should execute block and throw exception in block`() {
        shouldThrow<RuntimeException> {
            authorize("not yolo", `string should be yolo`) {
                if (it.decision == PolicyDecision.PERMIT) {
                    "my receiver"
                } else {
                    throw RuntimeException(it.reason)
                }
            }
        }
    }
}
