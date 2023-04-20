package it.unibo.jakta.agents.examples

import it.unibo.jakta.agents.bdi.dsl.mas
import it.unibo.jakta.agents.bdi.executionstrategies.ExecutionStrategy

fun main() {
    mas {
        environment {
            actions {
                action("thread", 0) {
                    println("${this.sender} thread: ${Thread.currentThread().name}")
                }
            }
        }
        agent("alice") {
            goals {
                achieve("my_thread")
            }
            plans {
                +achieve("my_thread") then {
                    execute("thread")
                }
            }
        }
        agent("bob") {
            goals {
                achieve("print_thread")
            }
            plans {
                +achieve("print_thread") then {
                    execute("thread")
                }
            }
        }
        executionStrategy {
            ExecutionStrategy.oneThreadPerAgent()
        }
    }.start()
}
