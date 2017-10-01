package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

class Extension
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Extension")
        w.writeEndElement()
    }

    fun validate(): Extension {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Extension? {
            if (xml == null) return null

            val ret = Extension()

            return ret.validate()
        }
    }
}