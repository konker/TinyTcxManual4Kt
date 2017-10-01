package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class NextSport(
        var Transition: ActivityLap? = null,
        var Activity: Activity
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("NextSport")
        Transition?.toXml(w)
        Activity.toXml(w)
        w.writeEndElement()
    }

    fun validate(): NextSport {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): NextSport? {
            if (xml == null) return null

            val ret = NextSport(
                    ActivityLap.fromXml(getElement(xml, "Transition")),
                    Activity.fromXml(getElement(xml, "Activity"))!!
            )

            return ret.validate()
        }
    }
}