package it.unibo.jakta.agents.examples

import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.bdi.executionstrategies.ExecutionStrategy
import it.unibo.jakta.agents.fsm.time.SimulatedTime
import it.unibo.jakta.agents.fsm.time.Time

fun main() {
    mas {
        agent("alice") {
            goals {
                achieve("time")
            }
            actions {
                action("time", 0) {
                    println("time: ${this.requestTimestamp}")
                }
            }
            plans {
                +achieve("time") then {
                    iact("time")
                    achieve("time")
                }
            }
            timeDistribution {
                Time.continuous((it as SimulatedTime).value + 5.0)
            }
        }
        executionStrategy {
            ExecutionStrategy.discreteEventExecution()
        }
    }.start()
}
