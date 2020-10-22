package io.kauthz.enforcement

import io.kauthz.specification.Policy

data class Authorization(
    val policy: Policy<*>,
    val func: () -> Unit,
)


