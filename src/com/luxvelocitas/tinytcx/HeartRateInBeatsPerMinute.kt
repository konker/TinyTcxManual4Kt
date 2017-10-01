package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class HeartRateInBeatsPerMinute(
    var Value: Short
)
{
    fun toXml(w: XMLStreamWriter, tagName: String) {
        w.writeStartElement(tagName)
        writeBasicElement(w, "Value", Value)
        w.writeEndElement()
    }

    fun validate(): HeartRateInBeatsPerMinute {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): HeartRateInBeatsPerMinute? {
            if (xml == null) return null

            val ret = HeartRateInBeatsPerMinute(
                    getValueShort(xml, "Value")!!
            )

            return ret.validate()
        }
    }
}