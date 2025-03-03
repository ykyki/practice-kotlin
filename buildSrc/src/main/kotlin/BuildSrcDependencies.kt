@file:Suppress("ConstPropertyName")

object Versions {
    const val Java = "21"

    const val Junit = "5.10.3"
    const val JunitPlatform = "1.10.3" // Jupiter と同じマイナー・パッチバージョン
}

object Libs {
    const val JunitJupiter = "org.junit.jupiter:junit-jupiter:${Versions.Junit}"
    const val JunitPlatformLauncher = "org.junit.platform:junit-platform-launcher:${Versions.JunitPlatform}"
}