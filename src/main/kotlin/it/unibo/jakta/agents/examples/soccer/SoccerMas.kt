@file:JvmName("SoccersMas")

package it.unibo.jakta.agents.examples.soccer

import it.unibo.jakta.agents.bdi.dsl.beliefs.fromSelf
import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.examples.soccer.Literals.print
import it.unibo.jakta.agents.examples.soccer.Literals.squad
import it.unibo.jakta.agents.examples.soccer.Literals.start
import java.net.URI

fun main() {
    mas { // BDI specification
    fun allPlayers(team: String) =
        Regex("""<a\s(\X*?)\sdata-cy="player">(.*)<\/a>""") // Object-oriented Regex library
            .findAll(URI("https://www.besoccer.com/team/squad/$team").toURL().readText())
            .map { team to it.groupValues[2] } // Monadic manipulation via lambda expression (functional style)

        listOf("napoli", "milan", "internazionale") // All the Kotlin libraries can be freely used
            .flatMap(::allPlayers) // Higher-order function (Functional style)
            .forEach { (team, player) -> // Destructuring declaration
                agent(player) {
                    beliefs {
                        fact { squad(team) }
                    }
                    goals {
                        achieve(start)
                    }
                    plans {
                        +achieve(start) onlyIf { squad(S).fromSelf } then {
                            execute(print("Hello! I play for", S))
                        }
                    }
                }
            }
    }.start()
}
