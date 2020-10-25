package io.kauthz.specification.demo

import io.kauthz.specification.PolicyDecision.DENY
import io.kauthz.specification.PolicyDecision.PERMIT
import io.kauthz.specification.can
import io.kauthz.specification.demo.policy.PolicyInternKode6.Companion.`subject kan behandle person med kode6`
import io.kauthz.specification.demo.policy.PolicyInternKode6.Roles.KODE6_GRUPPE
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PolicyTest {
    @Test
    fun `intern kode6 policy test`() {
        val subjectHarRolleOgPersonHarKode6 = context(SubjectType.InternBruker, listOf(KODE6_GRUPPE), person(spesReg = 6))
        (subjectHarRolleOgPersonHarKode6 can `subject kan behandle person med kode6`).decision shouldBe PERMIT

        val subjectHarIkkeRolleOgPersonHarKode6 = context(SubjectType.InternBruker, emptyList(), person(spesReg = 6))
        (subjectHarIkkeRolleOgPersonHarKode6 can `subject kan behandle person med kode6`).decision shouldBe DENY

        val personHarIkkeKode6 = context(SubjectType.InternBruker, emptyList(), person())
        (personHarIkkeKode6 can `subject kan behandle person med kode6`).decision shouldBe PERMIT
    }
}

private fun context(subjectType: SubjectType, subjectRoles: List<String> = emptyList(), person: Person) =
    PolicyContext(
        Subject(
            "someuser",
            subjectType,
            subjectRoles
        ),
        person
    )

private fun person(fnr: String = "12345678910", spesReg: Int = 0) =
    Person(fnr, spesReg)
