plugins {
    id("io.codearte.nexus-staging") version "0.21.2"
}
nexusStaging {
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")
    packageGroup = "no.nav"
    delayBetweenRetriesInMillis = 5000
}
