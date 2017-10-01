package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Position(
    var LatitudeDegrees: Double,
    var LongitudeDegrees: Double
)
{
    fun toXml(w: XMLStreamWriter, tagName: String) {
        w.writeStartElement(tagName)
        writeBasicElement(w, "LatitudeDegrees", LatitudeDegrees)
        writeBasicElement(w, "LongitudeDegrees", LongitudeDegrees)
        w.writeEndElement()
    }

    fun validate(): Position {
        if (LatitudeDegrees < -90 || LatitudeDegrees > 90) {
            throw TcxValidationException("LatitudeDegrees out of range: $LatitudeDegrees")
        }
        if (LongitudeDegrees < -180 || LongitudeDegrees > 180) {
            throw TcxValidationException("LongitudeDegrees out of range: $LongitudeDegrees")
        }
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Position? {
            if (xml == null) return null

            val ret = Position(
                    getValueDouble(xml, "LatitudeDegrees")!!,
                    getValueDouble(xml, "LongitudeDegrees")!!
            )

            return ret.validate()
        }
    }
}