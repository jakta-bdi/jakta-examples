package it.unibo.jakta.agents.examples.blocksworld.planbodyfactory

import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.examples.blocksworld.Environment
import it.unibo.tuprolog.core.Atom

fun main() {
    mas {
        environment {
            from(Environment())
            actions {
                action("move", 2) {
                    val upper: Atom = argument(0)
                    val lower: Atom = argument(1)
                    this.updateData(mapOf("upper" to upper, "lower" to lower))
                }
            }
        }

        agent("block-agent") {
            beliefs {
                rule("clear"(X) impliedBy (not("on"("source"("percept"), `_`, X)) or "table"))
            }
            goals {
                achieve("solve"("on"("a", "b"), "on"("b", "c"), "on"("c", "table")))
            }
            plans {
                +achieve("solve"("on"(X, Y), "on"(Y, Z), "on"(Z, T))) then {
                    // --------------------------------------------------------------------------------------------
                    from(PlanBodyFactory.smartStrategy("solve"("on"(X, Y), "on"(Y, Z), "on"(Z, T))))
                    // from(PlanBodyFactory.dummyStrategy("solve"("on"(X, Y), "on"(Y, Z), "on"(Z, T))))
                    // --------------------------------------------------------------------------------------------
                    iact("print"("End!"))
                }

                +achieve("move"(X, Y)) onlyIf {
                    "on"("source"("percept"), X, Y)
                }

                +achieve("move"(X, Y)) onlyIf {
                    "clear"("source"("self"), X) and "clear"("source"("self"), Y)
                } then {
                    execute("move"(X, Y))
                }

                +achieve("move"(X, Y)) onlyIf {
                    "clear"("source"("self"), X) and "on"("source"("percept"), W, Y)
                } then {
                    achieve("move"(W, "table"))
                    execute("move"(X, Y))
                }

                +achieve("move"(X, Y)) onlyIf {
                    "on"("source"("percept"), W, X) and "clear"("source"("self"), Y)
                } then {
                    achieve("move"(W, "table"))
                    execute("move"(X, Y))
                }

                +achieve("move"(X, Y)) onlyIf {
                    "on"("source"("percept"), W, X) and "on"("source"("percept"), V, Y)
                } then {
                    achieve("move"(W, "table"))
                    achieve("move"(V, "table"))
                    execute("move"(X, Y))
                }
            }
        }
    }.start()
}
