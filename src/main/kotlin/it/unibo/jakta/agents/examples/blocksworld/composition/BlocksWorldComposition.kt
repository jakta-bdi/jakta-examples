package it.unibo.jakta.agents.examples.blocksworld.composition

import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.bdi.dsl.plans
import it.unibo.jakta.agents.bdi.goals.Achieve
import it.unibo.jakta.agents.bdi.goals.Goal
import it.unibo.jakta.agents.bdi.plans.Plan
import it.unibo.jakta.agents.examples.blocksworld.Environment
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.a
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.b
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.c
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.clear
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.move
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.on
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.percept
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.self
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.solve
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.source
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.table
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.x
import it.unibo.jakta.agents.examples.blocksworld.composition.Literals.y
import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.dsl.lp

fun getPlans(): Iterable<Plan> = plans {
    +achieve(move(X, Y)) onlyIf {
        on(source(percept), X, Y)
    }
    +achieve(move(X, Y)) onlyIf {
        clear(source(self), X) and clear(source(self), Y)
    } then {
        execute(move(X, Y))
    }
    +achieve(move(X, Y)) onlyIf {
        clear(source(self), X) and on(source(percept), W, Y)
    } then {
        achieve(move(W, "table"))
        execute(move(X, Y))
    }
    +achieve(move(X, Y)) onlyIf {
        on(source(percept), W, X) and clear(source(self), Y)
    } then {
        achieve(move(W, table))
        execute(move(X, Y))
    }
    +achieve("move"(X, Y)) onlyIf {
        "on"("source"("percept"), W, X) and "on"("source"("percept"), V, Y)
    } then {
        achieve("move"(W, "table"))
        achieve("move"(V, "table"))
        execute("move"(X, Y))
    }
}

fun getPlanBody(solutionGoal: Struct): List<Goal> = solutionGoal.args.reversed().map { solution ->
    Achieve.of(Struct.of("move", solution.castToStruct().args))
}

fun main() {
    val goal: Struct = lp {
        solve(on(y, x), on(x, a), on(a, b), on(b, c), on(c, table))
    }
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
                achieve(goal)
            }
            plans(getPlans())
            plans {
                +achieve(goal) then {
                    from(getPlanBody(goal))
                    iact("print"("Done!"))
                }
            }
        }
    }.start()
}
