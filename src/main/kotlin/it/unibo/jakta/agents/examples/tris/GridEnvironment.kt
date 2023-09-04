package it.unibo.jakta.agents.examples.tris

import it.unibo.jakta.agents.bdi.AgentID
import it.unibo.jakta.agents.bdi.actions.ExternalAction
import it.unibo.jakta.agents.bdi.beliefs.Belief
import it.unibo.jakta.agents.bdi.beliefs.BeliefBase
import it.unibo.jakta.agents.bdi.environment.Environment
import it.unibo.jakta.agents.bdi.environment.impl.EnvironmentImpl
import it.unibo.jakta.agents.bdi.messages.MessageQueue
import it.unibo.jakta.agents.bdi.perception.Perception
import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Integer
import it.unibo.tuprolog.core.Struct

class GridEnvironment(
    private val n: Int,
    agentIDs: Map<String, AgentID> = emptyMap(),
    externalActions: Map<String, ExternalAction> = emptyMap(),
    messageBoxes: Map<AgentID, MessageQueue> = emptyMap(),
    perception: Perception = Perception.of(
        Belief.fromPerceptSource(Struct.of("turn", Atom.of("x"))),
    ),
    data: Map<String, Any> = mapOf("grid" to createGrid(n), "turn" to "x"),
) : EnvironmentImpl(externalActions, agentIDs, messageBoxes, perception, data) {

    companion object {
        internal fun createGrid(n: Int): Array<CharArray> {
            Array(n) { CharArray(n) { 'e' } }
            return arrayOf(
                charArrayOf('e', 'e', 'e'),
                charArrayOf('x', 'e', 'e'),
                charArrayOf('e', 'e', 'e'),
            )
        }

        internal fun Array<CharArray>.copy() =
            Array(size) { i -> CharArray(this[i].size) { j -> this[i][j] } }
    }

    @Suppress("UNCHECKED_CAST")
    internal val grid: Array<CharArray>
        get() = data["grid"] as Array<CharArray>

    @Suppress("UNCHECKED_CAST")
    override fun updateData(newData: Map<String, Any>): Environment {
        var newEnv = this
        if ("cell" in newData) {
            val cell = newData["cell"] as Triple<Int, Int, Char>
            val result = computeNextTurnEnvironment()
            result.grid[cell.first][cell.second] = cell.third
            result.grid.map { println(it) }
            newEnv = result
        }
        if ("changeTurn" in newData) {
            newEnv = computeNextTurnEnvironment()
        }
        return newEnv
    }

    private fun computeNextTurnEnvironment(): GridEnvironment {
        val actualTurn = data["turn"] as String
        val nextTurn = if (actualTurn == "x") "o" else "x"
        return copy(
            data = mapOf("grid" to grid.copy(), "turn" to nextTurn),
            perception = Perception.of(
                Belief.fromPerceptSource(Struct.of("turn", Atom.of(nextTurn))),
            ),
        )
    }

    override fun percept(): BeliefBase =
        BeliefBase.of(
            buildList {
                for (i in grid.indices) {
                    for (j in grid[i].indices) {
                        add(
                            Belief.fromPerceptSource(
                                Struct.of("cell", Integer.of(i), Integer.of(j), Atom.of("${grid[i][j]}")),
                            ),
                        )
                    }
                }
            },
        ).addAll(perception.percept()).updatedBeliefBase

    override fun copy(
        agentIDs: Map<String, AgentID>,
        externalActions: Map<String, ExternalAction>,
        messageBoxes: Map<AgentID, MessageQueue>,
        perception: Perception,
        data: Map<String, Any>,
    ) = GridEnvironment(n, agentIDs, externalActions, messageBoxes, perception, data)
}
