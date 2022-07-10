package com.example.nyethack

import java.io.File

class Player(_name: String,
             override var healthPoints: Int = 100,
             val isBlessed: Boolean,
             private val isImmortal: Boolean) : Fightable {

    var name = _name.trim()
        get() = "${field.capitalize()} of $hometown"
        set(value) {
            field = value.trim()
        }
    val hometown by lazy { selectHomeTown() }
    var currentPosition = Coordinate(0, 0)

    init {
        require(healthPoints > 0, { "healthPoints must be greater than zero." })
        require(name.isNotBlank(), { "Player must have a name. " })
    }

    constructor(name: String) : this(name,
        isBlessed = true,
        isImmortal = false) {
        if (name.toLowerCase() == "kar") healthPoints = 40
    }

    private fun selectHomeTown() = File("d:/Android/data/towns.txt")
        .readText()
        .split("\r\n").random()

    fun auraColor(): String {
        val auraVisible = isBlessed && healthPoints > 50 || isImmortal
        val auraColor = if (auraVisible) "GREEN" else "NONE"
        return auraColor
    }

    fun formatHealthStatus() =
        when (healthPoints) {
            100 -> "is in exellent condition!"
            in 90..99 -> "has a few scratches."
            in 75..89 -> if (isBlessed) {
                "has some minor wounds, but is healing quite quickly!"
            } else {
                "has some minor wounds."
            }
            in 15..74 -> "looks pretty hurt."
            else -> "is in awful condition!"
        }

    fun castFireball(numFireballs: Int = 2) =
        println("A glass of Fireball springs into existence. (x$numFireballs)")

    override val diceCount: Int = 3
    override val diceSides: Int = 6
    override fun attack(opponent: Fightable): Int {
        val damageDealt = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damageDealt
        return damageDealt
    }
}

open class Weapon(val name: String, val type: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Weapon) return false

        if (name != other.name) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}