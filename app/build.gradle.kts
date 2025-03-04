plugins {
    id("kotlin-conventions")
    application
}

dependencies {
    implementation(libs.apache.commons.text)
}

application {
    mainClass = "practice.kotlin.app.AppKt"
}
