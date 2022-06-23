import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val githubPassword: String by project

object Versions {
    const val graphQLKotlin = "5.3.2"
    const val ktor = "1.6.8"
    const val logback = "1.2.11"
    const val logstash = "7.1.1"
    const val kotlin = "1.6.21"
    const val tokenprovider = "0.1.4"
}

plugins {
    id("maven-publish")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("com.expediagroup.graphql") version "5.3.2"
    id("org.jmailen.kotlinter") version "3.10.0"
}

group = "no.nav.helsearbeidsgiver"
version = "0.2.2"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks {
    test {
        useJUnitPlatform()
    }
    lintKotlinMain {
        exclude("no/nav/helsearbeidsgiver/saf/graphql/generated/**/*.kt")
    }
    formatKotlinMain {
        exclude("no/nav/helsearbeidsgiver/saf/graphql/generated/**/*.kt")
    }
}

repositories {
    mavenCentral()
    maven {
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: "x-access-token"
            password = System.getenv("GITHUB_TOKEN") ?: githubPassword
        }
        setUrl("https://maven.pkg.github.com/navikt/*")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/helsearbeidsgiver-${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    runtimeOnly("ch.qos.logback:logback-classic:${Versions.logback}")

    implementation("com.expediagroup:graphql-kotlin-ktor-client:${Versions.graphQLKotlin}")
    implementation("io.ktor:ktor-client-core:${Versions.ktor}")
    implementation("io.ktor:ktor-client-json:${Versions.ktor}")
    implementation("io.ktor:ktor-client-serialization:${Versions.ktor}")
    implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
    testImplementation("io.ktor:ktor-client-mock:${Versions.ktor}")
    implementation("no.nav.helsearbeidsgiver:tokenprovider:${Versions.tokenprovider}")
}

graphql {
    client {
        sdlEndpoint = "https://navikt.github.io/saf/saf-api-sdl.graphqls"
        packageName = "no.nav.helsearbeidsgiver.saf.graphql.generated"
        schemaFile = File("src/main/resources/saf-api-sdl.graphqls")
        queryFiles = file("src/main/resources/saf").listFiles().toList()
        // allowDeprecatedFields = false
        serializer = com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer.KOTLINX
    }
}
