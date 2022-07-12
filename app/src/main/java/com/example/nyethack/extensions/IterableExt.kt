fun <T> Iterable<T>.random(): T = this.shuffled().first()

fun String.toDragonSpeak() = this.replace(Regex("[AEIOUaeiou]")) {
    when (it.value) {
        "A", "a" -> "4"
        "E", "e" -> "3"
        "I", "i" -> "1"
        "O", "o" -> "0"
        "U", "u" -> "|_|"
        else -> it.value
    }
}

fun frame(name: String, padding: Int, formatChar: String = "*"): String {
    val greeting = "$name!"
    val middle = formatChar.padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (0 until middle.length).joinToString("") { formatChar }
    return "$end\n$middle\n$end"
}