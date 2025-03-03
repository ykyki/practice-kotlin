plugins {
    id(Plugins.KotlinCorePlugin)
    application
}

dependencies {
    implementation(libs.apache.commons.text)
}

application {
    mainClass = "practice.kotlin.app.AppKt"
}
