plugins {
    kotlin("jvm")
    // id("org.gradle.toolchains.foojay-resolver-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(Libs.JunitJupiter)
    testRuntimeOnly(Libs.JunitPlatformLauncher)
}

// java {
//     toolchain {
//         languageVersion = JavaLanguageVersion.of(21)
//     }
// }
kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(Versions.Java)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
