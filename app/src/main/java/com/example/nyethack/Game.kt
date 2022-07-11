package com.example.nyethack

import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Game.play()
}

object Game {
    private val player = Player("Madrigal")
    private var currentRoom: Room = TownSquare()
    private var gameProcess = true

    private var worldMap = listOf(
        listOf(currentRoom, Room("Tavern"), Room("Back Room")),
        listOf(Room("Long Corridor"), Room("Generic Room")))

    init {
        println("Welcome, adventurer.")
        player.castFireball()
    }

    fun play() {
        while (gameProcess) {
            println(currentRoom.description())
            println(currentRoom.load())
            printPlayerStatus(player)
            print("> Enter your command: ")
            println(GameInput(readLine()).processCommand())
            currentRoom.configurePitGoblin { goblin ->
                goblin.healthPoints = dangerLevel * 3
                goblin
            }
        }
    }

    private fun printPlayerStatus(player: Player) {
        println("(Aura: ${player.auraColor()}) " +
                "(Blessed: ${if (player.isBlessed) "YES" else "NO"})")
        println("${player.name} ${player.formatHealthStatus()}")
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, {""})

        fun processCommand() = when (command.lowercase()) {
            "move" -> move(argument)
            "quit" -> gameOver()
            "map" -> map()
            "ring" -> ring(currentRoom)
            "fight" -> fight()
            else -> commandNotFound()
        }
        private fun commandNotFound() = "I'm not quite sure what you're trying to do!"
    }

    private fun move(directionInput: String) =
        try {
            val direction = Direction.valueOf(directionInput.uppercase())
            val newPosition = direction.updateCoordinate(player.currentPosition)
            if (!newPosition.isInBounds) {
                throw IllegalStateException("$direction is out of bounds.")
            }
            val newRoom = worldMap[newPosition.y][newPosition.x]
            player.currentPosition = newPosition
            currentRoom = newRoom
            "OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"

        } catch (e: Exception) {
            "Invalid direction: $directionInput."
        }

    private fun gameOver() {
        println("The GAME is OVER! Good bye!")
        gameProcess = false
    }

    private fun map() {
        var indexX = 0
        var indexY = 0
        for (indexY in 0..worldMap.size - 1) {
            for (indexX in 0..worldMap[indexY].size - 1) {
                if (worldMap[indexY][indexX] == currentRoom) {
                    print("X ")
                } else {
                    print("O ")
                }
            }
            println()
        }
    }

    private fun ring(place: Room) {
        if (place is TownSquare) {
            place.ringBell()
        } else {
            println("There is no bells in this room!")
        }
    }

    private fun fight() = currentRoom.monster?.let {
        while (player.healthPoints > 0 && it.healthPoints >0) {
            slay(it)
            Thread.sleep(1000)
        }
        "Combat complete."
    } ?: "There's nothing here to fight."

    private fun slay(monster: Monster) {
        println("${monster.name} did ${monster.attack(player)} damage!")
        println("${player.name} did ${player.attack(monster)} damage!")

        if (player.healthPoints <= 0) {
            println(">>> You have been defeated! Thanks for playing. <<<")
            exitProcess(0)
        }

        if (monster.healthPoints <= 0) {
            println(">>> ${monster.name} has been defeated! <<<")
            currentRoom.monster = null
        }

    }

}
