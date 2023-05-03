package it.unibo.jakta.agents.bdi.dsl.examples.tris

import it.unibo.jakta.agents.bdi.Jakta
import it.unibo.jakta.agents.bdi.dsl.MasScope
import it.unibo.jakta.agents.bdi.dsl.beliefs.BeliefsScope
import it.unibo.jakta.agents.bdi.dsl.beliefs.fromSelf
import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.bdi.dsl.plans.BodyScope
import it.unibo.jakta.agents.bdi.dsl.plans.PlansScope
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.End
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.aligned
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.allPossibleCombinationsOf
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.antidiagonal
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.cell
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.diagonal
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.e
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.horizontal
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.invoke
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.o
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.turn
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.vertical
import it.unibo.jakta.agents.examples.tris.TicTacToeLiterals.x
import it.unibo.tuprolog.core.Atom

fun BeliefsScope.alignment(name: String, dx: Int, dy: Int) {
    val first = cell(A, B, C)
    val second = cell(X, Y, Z)
    rule { name(listOf(second)).fromSelf impliedBy second }
    rule {
        name(listFrom(first, second, last = W)).fromSelf.impliedBy(
            // write("Handling  $name("), write(listFrom(first, second, last = W)), write(")"), nl,
            first,
            second,
            (X - A) arithEq dx,
            (Y - B) arithEq dy,
            name(listFrom(second, last = W)).fromSelf,
        )
    }
}

fun ticTacToe(n: Int = 3) = mas {
    require(n > 0)
    environment {
        from(GridEnvironment(n))
        actions {
            action(Put)
            action("End", 1) {
                val message = argument<Atom>(0).value
                println("[${this.sender}] $message")
                this.updateData(mapOf("changeTurn" to "other"))
                this.removeAgent(this.sender)
            }
        }
    }
    player(mySymbol = o, otherSymbol = x, gridSize = n)
    player(mySymbol = x, otherSymbol = o, gridSize = n)
}

fun MasScope.player(mySymbol: String, otherSymbol: String, gridSize: Int) = agent("$mySymbol-agent") {
    beliefs {
        alignment(vertical, dx = 0, dy = 1)
        alignment(horizontal, dx = 1, dy = 0)
        alignment(diagonal, dx = 1, dy = 1)
        alignment(antidiagonal, dx = 1, dy = -1)
        for (direction in arrayOf(vertical, horizontal, diagonal, antidiagonal)) {
            rule { aligned(L) impliedBy direction(L).fromSelf }
        }
    }
    plans {
        detectVictory(mySymbol, gridSize) // plan 1
        detectDefeat(mySymbol, otherSymbol, gridSize) // plan 2
        makeWinningMove(mySymbol, gridSize) // plan 3
        preventOtherFromWinning(mySymbol, otherSymbol, gridSize) // plan 4
        randomMove(mySymbol) // plan 5
    }
}

fun PlansScope.detectVictory(mySymbol: String, gridSize: Int) =
    detectLine(mySymbol, mySymbol, gridSize) { execute(End("I won!")) }
fun PlansScope.detectDefeat(mySymbol: String, otherSymbol: String, gridSize: Int) =
    detectLine(mySymbol, otherSymbol, gridSize) { execute(End("I lost!")) }

fun PlansScope.detectLine(mySymbol: String, symbol: String, size: Int, action: BodyScope.() -> Unit) =
    +turn(mySymbol) onlyIf { aligned((1..size).map { cell(symbol) }) } then(action)

fun PlansScope.makeWinningMove(mySymbol: String, gridSize: Int, symbol: String = mySymbol) {
    for (winningLine in allPossibleCombinationsOf(cell(X, Y, e), cell(symbol), gridSize - 1)) {
        +turn(mySymbol) onlyIf { aligned(winningLine) } then { Put(X, Y, mySymbol) }
    }
}

fun PlansScope.preventOtherFromWinning(mySymbol: String, otherSymbol: String, gridSize: Int) =
    makeWinningMove(mySymbol, gridSize, otherSymbol)

fun PlansScope.randomMove(mySymbol: String) =
    +turn(mySymbol) onlyIf { cell(X, Y, e) } then { Put(X, Y, mySymbol) }

fun main() {
    val system = ticTacToe(3)
    for (agent in system.agents) {
        Jakta.printAslSyntax(agent)
    }
    println("--------------------------------------------------------------------")
    system.start()
}
