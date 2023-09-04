@file:JvmName("FinitelyRecursiveAgent")

package it.unibo.jakta.agents.examples

import it.unibo.jakta.agents.bdi.dsl.mas

fun main() {
    mas {
        agent("alice") {
            goals {
                achieve("start"(0, 10))
            }
            plans {
                +achieve("start"(N, N)) then {
                    iact("print"("Hello World!", N))
                    iact("stop")
                }
                +achieve("start"(N, M)) onlyIf { (N lowerThan M) and (S `is` (N + 1)) } then {
                    iact("print"("Hello World!", N))
                    achieve("start"(S, M))
                }
            }
        }
    }.start()
}
