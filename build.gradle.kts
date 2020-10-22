val kotlinLoggingVersion = "2.0.3"
val logbackVersion = "1.2.3"
val junitJupiterVersion = "5.7.0"
val kotlinVersion = "1.4.10"
val kotestVersion = "4.3.0"

plugins {
    kotlin("jvm") version "1.4.0"
    id("org.jmailen.kotlinter") version "3.2.0"
    id("com.github.ben-manes.versions") version "0.33.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.15"
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
    withJavadocJar()
    withSourcesJar()
}

apply(plugin = "org.jmailen.kotlinter")

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion") // for kotest core jvm assertions
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks {
    withType<org.jmailen.gradle.kotlinter.tasks.LintTask> {
        dependsOn("formatKotlin")
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "14"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<Wrapper> {
        gradleVersion = "6.7"
    }
}
