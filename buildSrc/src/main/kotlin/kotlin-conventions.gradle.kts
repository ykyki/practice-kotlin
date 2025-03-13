group = "practice.kotlin"

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api(platform(libs.kotest.bom))
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
        vendor = JvmVendorSpec.ADOPTIUM
    }

    compilerOptions {
        freeCompilerArgs.add(
            "-Xconsistent-data-class-copy-visibility",
        )
    }

    // enable explicit api mode
    // cf: https://kotlinlang.org/docs/whatsnew14.html#mixing-named-and-positional-arguments
    explicitApi()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
