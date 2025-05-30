import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yaml"))
    parallel = true
    autoCorrect = true
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

tasks.named("check") {
    dependsOn("detektMain", "detektTest")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
