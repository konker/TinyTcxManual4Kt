package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Plan(
        var Type: TrainingTypeToken,
        var IntervalWorkout: Boolean,
        var Name: String?, //[TODO: len(1,15)]
        var Extensions: MutableList<Extension> = mutableListOf()
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Build")
        writeBasicElement(w, "IntervalWorkout", IntervalWorkout)
        writeBasicElement(w, "Name", Name)
        w.writeEndElement()
    }

    fun validate(): Plan {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Plan? {
            if (xml == null) return null

            val ret = Plan(
                    TrainingTypeToken.fromString(getValueString(xml, "Type"))!!,
                    getValueBoolean(xml, "IntervalWorkout")!!,
                    getValueString(xml, "String")
            )

            return ret.validate()
        }
    }
}