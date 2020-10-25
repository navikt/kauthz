package io.kauthz.xacml

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class RequestWrapperTest {

    @Test
    fun `deserialize XACML request`() {
        val mapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
        val req = mapper.readValue<RequestWrapper>(jsonRequest).request
        req.accessSubject?.get("urn:oasis:names:tc:xacml:1.0:subject:subject-id")?.shouldContainExactly("foobar")
        req.environment?.get("envCustomAttribute")?.shouldContainExactly("envCustomValue1", "envCustomValue2")
        req.action?.get("urn:oasis:names:tc:xacml:1.0:action:action-id")?.shouldContainExactly("read")
        req.resource?.get("urn:oasis:names:tc:xacml:1.0:resource:resource-id")?.shouldContainExactly("12345678910")
    }

    // language=JSON
    private val jsonRequest = """
        {
          "Request": {
            "AccessSubject": {
              "Attribute": [
                {
                  "AttributeId": "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
                  "Value": "foobar"
                }
              ]
            },
            "Environment": {
              "Attribute": [
                {
                  "AttributeId": "envCustomAttribute",
                  "Value": "envCustomValue1"
                },
                {
                  "AttributeId": "envCustomAttribute",
                  "Value": "envCustomValue2"
                }
              ]
            },
            "Action": {
              "Attribute": [
                {
                  "AttributeId": "urn:oasis:names:tc:xacml:1.0:action:action-id",
                  "Value": "read"
                }
              ]
            },
            "Resource": {
              "Attribute": [
                {
                  "AttributeId": "urn:oasis:names:tc:xacml:1.0:resource:resource-id",
                  "Value": "12345678910"
                }
              ]
            }
          }
        }
    """.trimIndent()
}
