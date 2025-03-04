plugins {
    id("kotlin-conventions")
    `java-library`
}

dependencies {
    implementation(platform(libs.arrow.stack))
    implementation(libs.bundles.arrow)
    implementation(libs.bundles.kotlinx.coroutines)
}
