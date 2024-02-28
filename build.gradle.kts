import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val graphQLKotlinVersion: String by project

val githubPassword: String by project

plugins {
    id("maven-publish")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.expediagroup.graphql")
    id("org.jmailen.kotlinter")
}

group = "no.nav.helsearbeidsgiver"
version = "1.0.0"

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
            username = "x-access-token"
            password = githubPassword
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
                username = "x-access-token"
                password = githubPassword
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.expediagroup:graphql-kotlin-ktor-client:$graphQLKotlinVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

graphql {
    client {
        sdlEndpoint = "https://navikt.github.io/saf/saf-api-sdl.graphqls"
        packageName = "no.nav.helsearbeidsgiver.saf.graphql.generated"
        schemaFile = File("src/main/resources/saf-api-sdl.graphqls")
        queryFiles = file("src/main/resources/saf").listFiles()?.toList()!!
        allowDeprecatedFields = true
        serializer = com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer.KOTLINX
    }
}
