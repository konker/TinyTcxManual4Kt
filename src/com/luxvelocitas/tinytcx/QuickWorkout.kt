package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class QuickWorkout(
    var TotalTimeSeconds: Double,
    var DistanceMeters: Double
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("QuickWorkout")
        writeBasicElement(w, "TotalTimeSeconds", TotalTimeSeconds)
        writeBasicElement(w, "DistanceMeters", DistanceMeters)
        w.writeEndElement()
    }

    fun validate(): QuickWorkout {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): QuickWorkout? {
            if (xml == null) return null

            val ret = QuickWorkout(
                    getValueDouble(xml, "TotalTimeSeconds")!!,
                    getValueDouble(xml, "DistanceMeters")!!
            )

            return ret.validate()
        }
    }
}