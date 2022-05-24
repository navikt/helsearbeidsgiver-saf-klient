import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project

plugins {
    id("maven-publish")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("org.jmailen.kotlinter") version "3.10.0"
    id("org.sonarqube") version "2.8"
 }

 sonarqube {
     properties {
         property("sonar.projectKey", "navikt_im-varsel")
         property("sonar.organization", "navikt")
         property("sonar.host.url", "https://sonarcloud.io")
         property("sonar.login", System.getenv("SONAR_TOKEN"))
     }
 }

group = "no.nav.helsearbeidsgiver"
version = "0.1.0"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
    maven {
        credentials {
            username = "x-access-token"
            password = System.getenv("GITHUB_TOKEN")
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
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("no.nav.helsearbeidsgiver:tokenprovider:0.1.3")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation(kotlin("test"))
}
