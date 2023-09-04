package it.unibo.jakta.agents.examples.blocksworld.planbodyfactory

import it.unibo.jakta.agents.bdi.goals.Achieve
import it.unibo.jakta.agents.bdi.goals.Goal
import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct

object PlanBodyFactory {
    fun smartStrategy(struct: Struct): List<Goal> {
        var list = listOf<Goal>()
        // stack from the bottom of the pile
        for (s in struct.args.reversed()) {
            list = list + Achieve.of(Struct.of("move", s.castToStruct().args))
        }
        return list
    }

    fun dummyStrategy(struct: Struct): List<Goal> {
        var list = listOf<Goal>()
        // clear all blocks
        for (s in struct.args) {
            list = list + Achieve.of(Struct.of("move", s.castToStruct().args.first(), Atom.of("table")))
        }
        // stack blocks
        for (s in struct.args.reversed()) {
            list = list + Achieve.of(Struct.of("move", s.castToStruct().args))
        }
        return list
    }
}
