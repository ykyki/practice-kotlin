plugins {
    id("kotlin-conventions")
    id("detekt-conventions")
    `java-library`
}

dependencies {
    implementation(libs.kotlin.result)
}

kotlin {
    explicitApi()
}
