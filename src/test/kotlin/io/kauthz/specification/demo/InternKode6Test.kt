package io.kauthz.specification.demo

import io.kauthz.specification.PolicyResult.DENY
import io.kauthz.specification.PolicyResult.PERMIT
import io.kauthz.specification.can
import io.kauthz.specification.demo.policy.Roles.KODE6_GRUPPE
import io.kauthz.specification.demo.policy.`subject kan behandle person med kode6`
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PolicyTest {
    @Test
    fun `intern kode6 policy test`() {
        val subjectHarRolleOgPersonHarKode6 = context(SubjectType.InternBruker, listOf(KODE6_GRUPPE), person(spesReg = 6))
        (subjectHarRolleOgPersonHarKode6 can `subject kan behandle person med kode6`).result shouldBe PERMIT

        val subjectHarIkkeRolleOgPersonHarKode6 = context(SubjectType.InternBruker, emptyList(), person(spesReg = 6))
        (subjectHarIkkeRolleOgPersonHarKode6 can `subject kan behandle person med kode6`).result shouldBe DENY

        val personHarIkkeKode6 = context(SubjectType.InternBruker, emptyList(), person())
        (personHarIkkeKode6 can `subject kan behandle person med kode6`).result shouldBe PERMIT
    }
}

private fun context(subjectType: SubjectType, subjectRoles: List<String> = emptyList(), person: Person) =
    AccessContext(
        Subject(
            "someuser",
            subjectType,
            subjectRoles
        ),
        person
    )

private fun person(fnr: String = "12345678910", spesReg: Int = 0) =
    Person(fnr, spesReg)
