@file:JvmName("ActionsOutputs")

package it.unibo.jakta.agents.examples

import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.tuprolog.core.Real
import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Var

fun main() {
    mas {
        agent("alice") {
            actions {
                action("sqrt", 2) {
                    val value = argument<Real>(0)
                    val output = argument<Var>(1)
                    this.addResults(
                        Substitution.unifier(output to Real.of(Math.sqrt(value.decimalValue.toDouble()))),
                    )
                    this.stopAgent()
                }
            }
            goals {
                achieve("start")
            }
            plans {
                +achieve("start") then {
                    execute("print"("Computing SQRT ..."))
                    execute("sqrt"(2.0, X))
                    execute("print"("The value is ", X))
                }
            }
        }
    }.start()
}
