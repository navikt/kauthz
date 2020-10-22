package io.kauthz.specification

import io.kauthz.specification.Policy.Companion.policy
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

        `string should be yolo`.permitOrFail("yolo") {
            "my receiver"
        } shouldBe "my receiver"

        shouldThrow<PolicyEvaluationException> {
            `string should be yolo`.permitOrFail("not yolo obviously") {
                "whatevs"
            }
        }
    }

    @Test
    fun `ifPermit should execute lambda and return receiver if result is Permit`(){
        `string should be yolo`.permit("yolo") {
            "my receiver"
        } shouldBe "my receiver"
    }

    @Test
    fun `ifPermit should not execute lambda and return PolicyEvaluation if result is NOT Permit`(){
        `string should be yolo`.permit("not yolo") {
            "my receiver"
        } should beInstanceOf<PolicyEvaluation>()

        (`string should be yolo`.permit("not yolo") {
            "my receiver"
        } as PolicyEvaluation).result shouldBe PolicyResult.DENY
    }
}
