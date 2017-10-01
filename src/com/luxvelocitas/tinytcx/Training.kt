package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Training(
        var QuickWorkoutResults: QuickWorkout,
        var Plan: Plan,
        var VirtualPartner: Boolean
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Training")
        QuickWorkoutResults.toXml(w)
        Plan.toXml(w)
        writeBasicElement(w, "VirtualPartner", VirtualPartner)
        w.writeEndElement()
    }

    fun validate(): Training {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Training? {
            if (xml == null) return null

            val ret = Training(
                    QuickWorkout.fromXml(getElement(xml, "QuickWorkoutResults"))!!,
                    Plan.fromXml(getElement(xml, "Plan"))!!,
                    getValueBoolean(xml, "VirtualPartner")!!
            )

            return ret.validate()
        }
    }
}