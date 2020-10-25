package io.kauthz.specification.demo

data class PolicyContext(
    val subject: Subject,
    val person: Person
)

data class Subject(
    val id: String,
    val type: SubjectType,
    val roles: List<String> = emptyList()
)

data class Person(
    val fnr: String,
    val spesReg: Int = 0
)

enum class SubjectType {
    InternBruker,
    EksternBruker,
    SystemBruker
}
