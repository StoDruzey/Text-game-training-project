package com.example.nyethack

data class Coordinate(var x: Int, var y: Int) {
    val isInBounds = x >= 0 && y >= 0
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
//    operator fun plusAssign(other: Coordinate) {
//        x += other.x
//        y += other.y
//    }
}

enum class Direction(private val coordinate: Coordinate) {
    NORTH(Coordinate(0, -1)),
    EAST(Coordinate(1,0)),
    SOUTH(Coordinate(0, 1)),
    WEST(Coordinate(-1, 0));

    fun updateCoordinate(playerCoordinate: Coordinate) =
        coordinate + playerCoordinate
}

//fun main() {
//    var c1 = Coordinate(2,3)
//    c1 += Coordinate(4, 5)
//    println(c1)
//}