package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class FirstSport(
    var Activity: Activity
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("FirstSport")
        Activity.toXml(w)
        w.writeEndElement()
    }

    fun validate(): FirstSport {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): FirstSport? {
            if (xml == null) return null

            val ret = FirstSport(
                    Activity.fromXml(getElement(xml, "Activity"))!!
            )

            return ret.validate()
        }
    }
}