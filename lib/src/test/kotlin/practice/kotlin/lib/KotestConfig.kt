package practice.kotlin.lib

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.property.PropertyTesting

class KotestConfig : AbstractProjectConfig() {
    override val parallelism = 4

    override suspend fun beforeProject() {
        PropertyTesting.defaultIterationCount = 1_000
        // PropertyTesting.shouldPrintGeneratedValues = true
    }
}
