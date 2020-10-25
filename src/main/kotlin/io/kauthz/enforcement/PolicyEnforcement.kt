package io.kauthz.enforcement

import io.kauthz.specification.Policy

data class Authorization<T>(
    val policy: Policy<T>,
    val block: () -> Unit
)

