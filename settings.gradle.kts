rootProject.name = "saf-klient"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotlinterVersion: String by settings
        val graphQLKotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jmailen.kotlinter") version kotlinterVersion
        id("com.expediagroup.graphql") version graphQLKotlinVersion
    }
}
