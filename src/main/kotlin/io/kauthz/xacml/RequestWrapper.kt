package io.kauthz.xacml

data class RequestWrapper(
    val request: Request
) {
    companion object {
        fun of(
            accessSubject: Category? = null,
            environment: Category? = null,
            action: Category? = null,
            resource: Category? = null,
        ) = RequestWrapper(
            Request(accessSubject, environment, action, resource)
        )
    }
}

data class Request(
    val accessSubject: Category? = null,
    val environment: Category? = null,
    val action: Category? = null,
    val resource: Category? = null,
) {
    fun accessSubject(vararg pairs: Pair<String, String>) = accessSubject?.add(*pairs) ?: Category.of(*pairs)
    fun environment(vararg pairs: Pair<String, String>) = environment?.add(*pairs) ?: Category.of(*pairs)
    fun action(vararg pairs: Pair<String, String>) = action?.add(*pairs) ?: Category.of(*pairs)
    fun resource(vararg pairs: Pair<String, String>) = resource?.add(*pairs) ?: Category.of(*pairs)
}


data class Category(
    val attribute: MutableList<Attribute> = mutableListOf()
) {
    operator fun get(key: String) = attribute.filter {
        key == it.attributeId
    }.map {
        it.value
    }

    fun add(vararg pairs: Pair<String, String>): Category = this.apply {
        attribute.addAll(
            pairs.map {
                Attribute(it.first, it.second)
            }.toMutableList()
        )
    }

    companion object {
        fun of(vararg pairs: Pair<String, String>) =
            Category(
                pairs.map {
                    Attribute(it.first, it.second)
                }.toMutableList()
            )
    }
}

data class Attribute(
    val attributeId: String,
    val value: String
)
