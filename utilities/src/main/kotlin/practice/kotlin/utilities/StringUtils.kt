/*
 * This source file was generated by the Gradle 'init' task
 */
package practice.kotlin.utilities

import practice.kotlin.list.LinkedList

class StringUtils {
    companion object {
        fun join(source: LinkedList): String {
            return JoinUtils.join(source)
        }

        fun split(source: String): LinkedList {
            return SplitUtils.split(source)
        }
    }
}
