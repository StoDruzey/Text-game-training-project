package com.example.nyethack
import java.io.File
import kotlin.math.roundToInt

const val TAVERN_NAME = "Taernyl's Folly"

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt")
    .readText()
    .split("\r\n")
val patronGold = mutableMapOf<String, Double>()

private fun <T> Iterable<T>.random(): T = this.shuffled().first()

fun main() {
    val greetingFunction: (String) -> String = {
        val currentYear = 2022
        "Welcome to SimVillage, $it! (copyright $currentYear)"
    }
    println(greetingFunction("Serge"))

    (0..9).forEach {
        val first = patronList.shuffled().first()
        val last = lastName.shuffled().first()
        val name = "$first $last"
        uniquePatrons += name
    }
    uniquePatrons.forEach {
        patronGold[it] = 6.0
    }
    var orderCount = 0
    while (orderCount <= 9) {
        placeOrder(uniquePatrons.shuffled().first(), menuList.shuffled().first())
        orderCount++
    }
    println(patronGold)
    displayPartnerBalances()
    tavernBouncer()
    //displayPartnerBalances()

//    val x = listOf(mutableListOf(1, 2, 3), mutableListOf(4, 5, 6))
//    println(x)
//    x[0].add(8)
//    x[1].add(9)
//    println(x)

    menuPrint(menuList)
//    val patronGold = mutableMapOf("Mordoc" to 6.0)
//    patronGold.putAll(listOf("Jebediah" to 5.0, "Sahara" to 6.0)) //почему в лист ложится пара?
//    println(patronGold["Jebediah"])
//    val patronGold = mutableListOf("Mordoc" to 6.0)
//    println(patronGold)


//    dragonsBreathReserve(5, 9)
}
private fun displayPartnerBalances() {
    patronGold.forEach { patron, balance ->
        println("$patron, balance: ${"%.2f".format(balance)}")
    }
}

private fun tavernBouncer() {
    val patronToDel = mutableListOf<String>()
    patronGold.forEach { patron, balance ->
        if (balance <= 0) {
            patronToDel.add(patron)
        }
    }
    patronToDel.forEach {
        patronGold.remove(it)
        uniquePatrons.remove(it)
    }
    println(uniquePatrons)
    println(patronGold)
}

private fun placeOrder(patronName: String, menuData: String) {
    val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)
    println("$patronName speaks with $tavernMaster about their order.")
//    val data = menuData.split(",")
    val (type, name, price) = menuData.split(",")
//    val type = data[0]
//    val name = data[1]
//    val price = data[2]
    val message = "$patronName byus a $name ($type) for $price"
    println(message)
    performPurchase(price.toDouble(), patronName)
    val phrase = if (name == "Dragon's Breath") {
        "$patronName exclaims: ${toDragonSpeak("Ah, delicious $name!")}"
        } else {
            "$patronName says: Thanks for the $name."
        }
    println(phrase)
//    if (performPurchase(price.toDouble())) {
//        val message = "$patronName byus a $name ($type) for $price"
//        println(message)
//        val phrase = if (name == "Dragon's Breath") {
//            "Madrigal exclaims: ${toDragonSpeak("Ah, DELICIOUS $name!")}"
//        } else {
//            "Madrigal says: Thanks for the $name."
//        }
//        println(phrase)
//    }
}

private fun toDragonSpeak(phrase: String) = phrase.replace(Regex("[AEIOUaeiou]")) {
    when (it.value) {
        "A", "a" -> "4"
        "E", "e" -> "3"
        "I", "i" -> "1"
        "O", "o" -> "0"
        "U", "u" -> "|_|"
        else -> it.value
    }
}

fun performPurchase(price: Double, patronName: String) {
    val totalPurse = patronGold.getValue(patronName)
    patronGold[patronName] = totalPurse - price
}

private fun dragonsBreathReserve(casksDelivered: Int, pintsDrunk: Int) {
    val PINT_VOLUME = 0.125
    var pintsRemain: Int = (casksDelivered * 5 / PINT_VOLUME).toInt()
    pintsRemain -= pintsDrunk
    var count = pintsRemain * PINT_VOLUME
    var casksRemain: Int = (count / 5).toInt()
    var gallonsRemain: Int = (count - casksRemain * 5).toInt()
    pintsRemain -= (casksRemain * 5 / PINT_VOLUME + gallonsRemain / PINT_VOLUME).toInt()
    println("Remain is: $casksRemain casks + $gallonsRemain gallons + $pintsRemain pints")
}

class Sword(_name: String) {
    var name = _name
        get() = "The Legendary $field"
        set(value) {
            field = value.toLowerCase().reversed().capitalize()
        }
    init {
        name = _name
    }
}

private fun menuPrint(menuData: List<String>) { //чистовая печать меню
    println("\n*** Welcome to $TAVERN_NAME ***\n") //печать заголовка меню
    var menuStrLen: Int = 0
    val menuTypeList = mutableSetOf<String>()
    menuData.forEach {
        if (it.length > menuStrLen) menuStrLen = it.length //найдем самую длинную строку, чтобы посчитать кол-во точек заполнения
        var (type, _, _) = it.split(",") //создаем лист типов блюд
        menuTypeList.add(type)
    }
    println(menuTypeList)
//    menuTypeList.distinct()
//    println(menuTypeList)
    for (menuType in menuTypeList) { //печатаем меню по типам блюд
        println("~[$menuType]~")
        menuData.forEach {
            var (type, name, price) = it.split(",")
            if (type == menuType) {
                while (name.length < menuStrLen) {
                    name += "."
                }
                println("${name.replaceFirstChar { it.titlecase() }}$price")
            }
        }
    }
    println()
}