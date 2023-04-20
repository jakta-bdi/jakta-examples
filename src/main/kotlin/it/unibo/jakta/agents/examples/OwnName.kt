package it.unibo.jakta.agents.examples

import kotlin.reflect.KProperty

object OwnName {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = property.name
}
