package practice.kotlin.app

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.WordUtils


fun main() {
    val tokens = StringUtils.split(MessageUtils.getMessage())
    val result = StringUtils.join(tokens)
    println(WordUtils.capitalize(result))
}
